package io.github.amichailides.merimna.employeePosition.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.LinkedHashMap;
import java.util.Map;

public class InvalidEmployeePositionCodeException extends BaseDomainException {
    public InvalidEmployeePositionCodeException(String value) {
        super(ErrorCode.INVALID_EMPLOYEE_POSITION_CODE, buildContext(value));
    }

    private static Map<String, Object> buildContext(String value) {
        Map<String, Object> context = new LinkedHashMap<>();

        if (value != null) {
            context.put("positionCode", value);
        }

        return context;
    }
}
