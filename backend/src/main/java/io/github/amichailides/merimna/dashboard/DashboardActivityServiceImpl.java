package io.github.amichailides.merimna.dashboard;

import io.github.amichailides.merimna.audit.AuditEntityType;
import io.github.amichailides.merimna.audit.AuditLog;
import io.github.amichailides.merimna.audit.AuditLogRepository;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import io.github.amichailides.merimna.dashboard.dto.DashboardActivityReadOnlyDTO;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardActivityServiceImpl implements DashboardActivityService {

    private static final int RECENT_ACTIVITY_LIMIT = 5;

    private final AuditLogRepository auditLogRepository;
    private final EmployeeRepository employeeRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DashboardActivityReadOnlyDTO> getRecentActivity() {
        List<AuditLog> auditLogs = auditLogRepository
                .findByEntityTypeNot(
                        AuditEntityType.AUTH,
                        PageRequest.of(
                                0,
                                RECENT_ACTIVITY_LIMIT,
                                Sort.by(Sort.Direction.DESC, "occurredAt")
                        )
                )
                .getContent();

        Set<UUID> employeePublicIds = auditLogs.stream()
                .map(this::getEmployeeSubjectPublicId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<UUID> beneficiaryPublicIds = auditLogs.stream()
                .map(this::getBeneficiarySubjectPublicId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<UUID, String> employeeNames =
                employeeRepository.findAllByPublicIdIn(employeePublicIds)
                        .stream()
                        .collect(Collectors.toMap(
                                employee -> employee.getPublicId(),
                                employee -> employee.getFirstName()
                                        + " "
                                        + employee.getLastName()
                        ));

        Map<UUID, String> beneficiaryNames =
                beneficiaryRepository.findAllByPublicIdIn(beneficiaryPublicIds)
                        .stream()
                        .collect(Collectors.toMap(
                                beneficiary -> beneficiary.getPublicId(),
                                beneficiary -> beneficiary.getFirstName()
                                        + " "
                                        + beneficiary.getLastName()
                        ));

        return auditLogs.stream()
                .map(auditLog -> toDTO(
                        auditLog,
                        employeeNames,
                        beneficiaryNames
                ))
                .toList();
    }

    private DashboardActivityReadOnlyDTO toDTO(
            AuditLog auditLog,
            Map<UUID, String> employeeNames,
            Map<UUID, String> beneficiaryNames
    ) {
        UUID employeePublicId = getEmployeeSubjectPublicId(auditLog);
        UUID beneficiaryPublicId = getBeneficiarySubjectPublicId(auditLog);

        String subjectName = null;

        if (employeePublicId != null) {
            subjectName = employeeNames.get(employeePublicId);
        } else if (beneficiaryPublicId != null) {
            subjectName = beneficiaryNames.get(beneficiaryPublicId);
        }

        return new DashboardActivityReadOnlyDTO(
                auditLog.getPublicId(),
                auditLog.getAction(),
                auditLog.getEntityType(),
                auditLog.getEntityPublicId(),
                auditLog.getOccurredAt(),
                auditLog.getMetadata(),
                subjectName
        );
    }

    private UUID getEmployeeSubjectPublicId(AuditLog auditLog) {
        return switch (auditLog.getEntityType()) {
            case EMPLOYEE -> auditLog.getEntityPublicId();

            case EMPLOYEE_ASSIGNMENT, EMPLOYEE_PLACEMENT ->
                    getMetadataUuid(auditLog, "employeePublicId");

            default -> null;
        };
    }

    private UUID getBeneficiarySubjectPublicId(AuditLog auditLog) {
        return switch (auditLog.getEntityType()) {
            case BENEFICIARY -> auditLog.getEntityPublicId();

            case MEDICATION ->
                    getMetadataUuid(auditLog, "beneficiaryPublicId");

            default -> null;
        };
    }

    private UUID getMetadataUuid(
            AuditLog auditLog,
            String key
    ) {
        if (auditLog.getMetadata() == null) {
            return null;
        }

        Object value = auditLog.getMetadata().get(key);

        if (value instanceof UUID uuid) {
            return uuid;
        }

        if (value instanceof String stringValue) {
            try {
                return UUID.fromString(stringValue);
            } catch (IllegalArgumentException ignored) {
                return null;
            }
        }

        return null;
    }
}
