package io.github.amichailides.merimna.employee.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.Employee;

import java.util.UUID;

public record EmployeeCreatedEvent(
        UUID employeePublicId
) implements AuditableEvent {

    public static EmployeeCreatedEvent from(Employee employee) {
        return new EmployeeCreatedEvent(employee.getPublicId());
    }

    @Override
    public AuditAction action() {
        return AuditAction.EMPLOYEE_CREATED;
    }

    @Override
    public UUID entityPublicId() {
        return employeePublicId;
    }
}
