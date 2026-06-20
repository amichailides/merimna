package io.github.amichailides.merimna.employee.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.Employee;

import java.util.UUID;

public record EmployeeReactivatedEvent(
        UUID employeePublicId
) implements AuditableEvent {

    public static EmployeeReactivatedEvent from(Employee employee) {
        return new EmployeeReactivatedEvent(
                employee.getPublicId()
        );
    }

    @Override
    public UUID entityPublicId() {
        return employeePublicId;
    }

    @Override
    public AuditAction action() {
        return AuditAction.EMPLOYEE_REACTIVATED;
    }

    @Override
    public UUID subjectEmployeePublicId() {
        return employeePublicId;
    }
}
