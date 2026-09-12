package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;

public class SameEmployeePositionException extends BaseDomainException {
    public SameEmployeePositionException(String code) {
        super(ErrorCode.SAME_EMPLOYEE_POSITION, Map.of(
                "positionCode", code
        ));
    }
}
