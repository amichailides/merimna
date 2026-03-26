package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeAlreadyInactiveException extends BaseDomainException {
    public EmployeeAlreadyInactiveException( ) {
        super(ErrorCode.EMPLOYEE_ALREADY_INACTIVE);
    }
}
