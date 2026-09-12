package io.github.amichailides.merimna.dashboard;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditLog;
import io.github.amichailides.merimna.audit.AuditLogRepository;
import io.github.amichailides.merimna.dashboard.dto.DashboardSecurityEventReadOnlyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DashboardSecurityEventServiceImpl implements DashboardSecurityEventService {

    private static final int SECURITY_EVENT_LIMIT = 5;

    private static final Set<AuditAction> SECURITY_ACTIONS = Set.of(
            AuditAction.AUTH_LOGIN_FAILED,
            AuditAction.AUTH_REFRESH_TOKEN_REUSE_DETECTED,
            AuditAction.AUTH_PASSWORD_CHANGED,
            AuditAction.AUTH_PASSWORD_RESET
    );

    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DashboardSecurityEventReadOnlyDTO> getRecentSecurityEvents() {
        return auditLogRepository.findByActionIn(
                        SECURITY_ACTIONS,
                        PageRequest.of(
                                0,
                                SECURITY_EVENT_LIMIT,
                                Sort.by(Sort.Direction.DESC, "occurredAt")
                        )
                )
                .getContent()
                .stream()
                .map(this::toReadOnlyDTO)
                .toList();
    }

    private DashboardSecurityEventReadOnlyDTO toReadOnlyDTO(AuditLog auditLog) {
        return new DashboardSecurityEventReadOnlyDTO(
                auditLog.getPublicId(),
                auditLog.getAction(),
                auditLog.getEntityPublicId(),
                auditLog.getOccurredAt(),
                auditLog.getUserPublicId(),
                auditLog.getEmployeePublicId(),
                auditLog.getMetadata()
        );
    }
}