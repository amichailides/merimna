package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.domain.Employee;

import java.util.Map;
import java.util.UUID;

public record EmployeeUpdatedEvent(
        UUID employeePublicId,
        EntityChangeSet changeSet
) implements AuditableEvent {

    public static EmployeeUpdatedEvent from(Employee employee, EntityChangeSet changeSet) {
        return new EmployeeUpdatedEvent(
                employee.getPublicId(),
                changeSet
        );
    }

    @Override
    public AuditAction action() {
        return AuditAction.EMPLOYEE_UPDATED;
    }

    @Override
    public UUID entityPublicId() {
        return employeePublicId;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "changes", changeSet.changes()
        );
    }
}
