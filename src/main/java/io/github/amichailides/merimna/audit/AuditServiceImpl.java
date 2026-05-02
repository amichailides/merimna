package io.github.amichailides.merimna.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService{

    private final AuditLogRepository auditLogRepository;
    private final AuditContext auditContext;

    @Override
    public void record(AuditableEvent event) {
        AuditLog auditLog = AuditLog.builder()
                .action(event.action())
                .entityType(event.action().getEntityType())
                .entityPublicId(event.entityPublicId())
                .userPublicId(auditContext.getUserPublicId())
                .employeePublicId(auditContext.getEmployeePublicId())
                .ipAddress(auditContext.getIpAddress())
                .userAgent(auditContext.getUserAgent())
                .outcome(AuditOutcome.SUCCESS)
                .metadata(event.metadata())
                .build();

        auditLogRepository.save(auditLog);
    }
}
