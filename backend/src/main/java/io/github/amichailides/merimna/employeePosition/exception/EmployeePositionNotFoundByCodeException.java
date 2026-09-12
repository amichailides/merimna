package io.github.amichailides.merimna.employeePosition.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

import java.util.Map;

public class EmployeePositionNotFoundByCodeException extends BaseApplicationException {
    public EmployeePositionNotFoundByCodeException(String code) {
        super(ErrorCode.EMPLOYEE_POSITION_NOT_FOUND_BY_CODE, Map.of(
                "positionCode", code
        ));
    }
}
