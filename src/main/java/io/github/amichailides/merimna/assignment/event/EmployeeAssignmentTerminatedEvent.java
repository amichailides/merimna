package io.github.amichailides.merimna.assignment.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.EmployeeAssignment;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record EmployeeAssignmentTerminatedEvent(
        UUID assignmentPublicId,
        UUID employeePublicId,
        UUID houseUnitPublicId,
        LocalDate startDate,
        LocalDate endDate
) implements AuditableEvent {

    public static EmployeeAssignmentTerminatedEvent from(EmployeeAssignment assignment) {
        return new EmployeeAssignmentTerminatedEvent(
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
        return AuditAction.ASSIGNMENT_TERMINATED;
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