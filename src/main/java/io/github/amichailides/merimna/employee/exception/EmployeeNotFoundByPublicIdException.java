package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeNotFoundByPublicIdException extends BaseDomainException {
    public EmployeeNotFoundByPublicIdException(String publicId)
    {
        super(ErrorCode.EMPLOYEE_NOT_FOUND_BY_ID, publicId);
    }
}
