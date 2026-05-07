package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

import java.util.UUID;

public class EmployeeNotFoundByPublicIdException extends BaseApplicationException {
    public EmployeeNotFoundByPublicIdException(UUID publicId)
    {
        super(ErrorCode.EMPLOYEE_NOT_FOUND_BY_PUBLIC_ID, publicId);
    }
}
