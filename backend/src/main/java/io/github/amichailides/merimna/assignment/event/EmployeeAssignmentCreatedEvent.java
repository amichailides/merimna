package io.github.amichailides.merimna.assignment.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.EmployeeAssignment;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record EmployeeAssignmentCreatedEvent(
        UUID assignmentPublicId,
        UUID employeePublicId,
        UUID houseUnitPublicId,
        String houseUnitCode,
        String houseUnitDisplayName,
        LocalDate startDate,
        LocalDate endDate
) implements AuditableEvent {

    public static EmployeeAssignmentCreatedEvent from(EmployeeAssignment assignment) {
        return new EmployeeAssignmentCreatedEvent(
                assignment.getPublicId(),
                assignment.getEmployee().getPublicId(),
                assignment.getHouseUnit().getPublicId(),
                assignment.getHouseUnit().getCode(),
                assignment.getHouseUnit().getDisplayName(),
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
    public UUID subjectEmployeePublicId() {
        return employeePublicId;
    }

    @Override
    public Map<String, Object> metadata() {
        return EmployeeAssignmentEventMetadata.of(
                employeePublicId,
                houseUnitPublicId,
                houseUnitCode,
                houseUnitDisplayName,
                startDate,
                endDate
        );
    }
}