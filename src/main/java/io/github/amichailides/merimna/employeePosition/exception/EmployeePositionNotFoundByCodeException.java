package io.github.amichailides.merimna.employeePosition.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeePositionNotFoundByCodeException extends BaseDomainException {
    public EmployeePositionNotFoundByCodeException(String code) {
        super(ErrorCode.EMPLOYEE_POSITION_NOT_FOUND_BY_CODE, code);
    }
}
