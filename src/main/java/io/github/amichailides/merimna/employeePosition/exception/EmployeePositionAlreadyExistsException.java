package io.github.amichailides.merimna.employeePosition.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeePositionAlreadyExistsException extends BaseDomainException {
    public EmployeePositionAlreadyExistsException(String code) {
        super(ErrorCode.EMPLOYEE_POSITION_ALREADY_EXISTS, code);
    }
}
