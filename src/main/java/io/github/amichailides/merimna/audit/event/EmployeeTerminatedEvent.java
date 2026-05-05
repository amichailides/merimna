package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.Employee;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record EmployeeTerminatedEvent(
        UUID employeePublicId,
        LocalDate terminationDate)
        implements AuditableEvent {

    public static EmployeeTerminatedEvent from(Employee employee, LocalDate terminationDate) {
        return new EmployeeTerminatedEvent(
                employee.getPublicId(),
                terminationDate);
    }

    @Override
    public AuditAction action() {
        return AuditAction.EMPLOYEE_TERMINATED;
    }

    @Override
    public UUID entityPublicId() {
        return employeePublicId;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "activeAfter", false,
                "terminationDate", terminationDate.toString()
        );
    }
}
