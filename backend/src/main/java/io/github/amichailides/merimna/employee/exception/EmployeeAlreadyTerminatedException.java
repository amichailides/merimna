package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;

public class EmployeeAlreadyTerminatedException extends BaseDomainException {
    public EmployeeAlreadyTerminatedException(Long id) {
        super(ErrorCode.EMPLOYEE_ALREADY_TERMINATED, Map.of(
                "employeeId", id
        ));
    }
}
