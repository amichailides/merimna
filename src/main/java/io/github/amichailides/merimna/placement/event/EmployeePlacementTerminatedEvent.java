package io.github.amichailides.merimna.placement.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.EmployeePlacement;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record EmployeePlacementTerminatedEvent(
        UUID placementPublicId,
        UUID employeePublicId,
        UUID houseUnitPublicId,
        LocalDate startDate,
        LocalDate endDate
) implements AuditableEvent {

    public static EmployeePlacementTerminatedEvent from(EmployeePlacement placement) {
        return new EmployeePlacementTerminatedEvent(
                placement.getPublicId(),
                placement.getEmployee().getPublicId(),
                placement.getHouseUnit().getPublicId(),
                placement.getStartDate(),
                placement.getEndDate()
        );
    }

    @Override
    public UUID entityPublicId() {
        return placementPublicId;
    }

    @Override
    public UUID subjectEmployeePublicId() {
        return employeePublicId;
    }

    @Override
    public AuditAction action() {
        return AuditAction.PLACEMENT_TERMINATED;
    }

    @Override
    public Map<String, Object> metadata() {
        return EmployeePlacementEventMetadata.of(
                employeePublicId,
                houseUnitPublicId,
                startDate,
                endDate
        );
    }
}