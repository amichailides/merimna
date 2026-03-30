package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeNotFoundByIdException extends BaseDomainException {
    public EmployeeNotFoundByIdException(Long employeeId)
    {
        super(ErrorCode.EMPLOYEE_NOT_FOUND_BY_ID, employeeId);
    }
}
