package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeAlreadyActiveException extends BaseDomainException {
    public EmployeeAlreadyActiveException()
    {
        super(ErrorCode.EMPLOYEE_ALREADY_ACTIVE);
    }
}
