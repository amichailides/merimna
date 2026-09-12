package io.github.amichailides.merimna.employee.systemaccess.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeAccountAlreadyExistsException
        extends BaseDomainException {

    public EmployeeAccountAlreadyExistsException() {
        super(ErrorCode.EMPLOYEE_ALREADY_HAS_ACCOUNT);
    }
}
