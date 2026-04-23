package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.UUID;

public class EmployeeNotFoundByPublicIdException extends BaseDomainException {
    public EmployeeNotFoundByPublicIdException(UUID publicId)
    {
        super(ErrorCode.EMPLOYEE_NOT_FOUND_BY_ID, publicId);
    }
}
