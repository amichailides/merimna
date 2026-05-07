package io.github.amichailides.merimna.employeePosition.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class EmployeePositionAlreadyExistsException extends BaseApplicationException {
    public EmployeePositionAlreadyExistsException(String code) {
        super(ErrorCode.EMPLOYEE_POSITION_ALREADY_EXISTS, code);
    }
}
