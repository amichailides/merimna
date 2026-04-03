package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeInactiveException extends BaseDomainException {
    public EmployeeInactiveException() {
        super(ErrorCode.EMPLOYEE_INACTIVE);
    }
}
