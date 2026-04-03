package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeAlreadyTerminatedException extends BaseDomainException {
    public EmployeeAlreadyTerminatedException(Long id) {
        super(ErrorCode.EMPLOYEE_ALREADY_TERMINATED, id);
    }
}
