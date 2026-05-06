package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.EmployeeAssignment;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public record EmployeeAssignmentCreatedEvent(
        UUID assignmentPublicId,
        UUID employeePublicId,
        UUID houseUnitPublicId,
        LocalDate startDate,
        LocalDate endDate
) implements AuditableEvent {

    public static EmployeeAssignmentCreatedEvent from(EmployeeAssignment assignment) {
        return new EmployeeAssignmentCreatedEvent(
                assignment.getPublicId(),
                assignment.getEmployee().getPublicId(),
                assignment.getHouseUnit().getPublicId(),
                assignment.getStartDate(),
                assignment.getEndDate()
        );
    }

    @Override
    public UUID entityPublicId() {
        return assignmentPublicId;
    }

    @Override
    public AuditAction action() {
        return AuditAction.ASSIGNMENT_CREATED;
    }

    @Override
    public Map<String, Object> metadata() {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("employeePublicId", employeePublicId);
        metadata.put("houseUnitPublicId", houseUnitPublicId);
        metadata.put("startDate", startDate.toString());
        metadata.put("endDate", endDate != null ? endDate.toString() : null);
        return metadata;
    }
}