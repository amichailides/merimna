package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.EmployeePosition;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class SameEmployeePositionException extends BaseDomainException {
    public SameEmployeePositionException(EmployeePosition currentPosition) {

        super(ErrorCode.SAME_EMPLOYEE_POSITION, currentPosition);
    }
}
