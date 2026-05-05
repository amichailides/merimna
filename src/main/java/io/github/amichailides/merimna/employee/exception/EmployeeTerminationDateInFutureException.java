package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeTerminationDateInFutureException extends BaseDomainException {
    public EmployeeTerminationDateInFutureException() {
        super(ErrorCode.EMPLOYEE_TERMINATION_DATE_IN_FUTURE);
    }
}
