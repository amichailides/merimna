package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.EmployeeAssignment;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record EmployeeAssignmentCancelledEvent(
        UUID assignmentPublicId,
        UUID employeePublicId,
        UUID houseUnitPublicId,
        LocalDate startDate,
        LocalDate endDate
) implements AuditableEvent {

    public static EmployeeAssignmentCancelledEvent from(EmployeeAssignment assignment) {
        return new EmployeeAssignmentCancelledEvent(
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
        return AuditAction.ASSIGNMENT_CANCELLED;
    }

    @Override
    public Map<String, Object> metadata() {
        return EmployeeAssignmentEventMetadata.of(
                employeePublicId,
                houseUnitPublicId,
                startDate,
                endDate
        );
    }
}