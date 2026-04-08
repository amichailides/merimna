package io.github.amichailides.merimna.employeePosition.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class InvalidEmployeePositionCodeException extends BaseDomainException {
    public InvalidEmployeePositionCodeException(String value) {
        super(ErrorCode.INVALID_EMPLOYEE_POSITION_CODE, value);
    }
}
