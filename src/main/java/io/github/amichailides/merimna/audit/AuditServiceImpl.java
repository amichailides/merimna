package io.github.amichailides.merimna.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final AuditContext auditContext;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(AuditableEvent event) {
        AuditLog auditLog = AuditLog.builder()
                .action(event.action())
                .entityType(event.action().getEntityType())
                .entityPublicId(event.entityPublicId())
                .userPublicId(event.actorUserPublicId() != null
                        ? event.actorUserPublicId()
                        : auditContext.getUserPublicId())
                .employeePublicId(event.actorEmployeePublicId() != null
                        ? event.actorEmployeePublicId()
                        : auditContext.getEmployeePublicId())
                .subjectEmployeePublicId(event.subjectEmployeePublicId())
                .ipAddress(auditContext.getIpAddress())
                .userAgent(auditContext.getUserAgent())
                .outcome(AuditOutcome.SUCCESS)
                .metadata(event.metadata())
                .build();

        auditLogRepository.save(auditLog);
    }
}
