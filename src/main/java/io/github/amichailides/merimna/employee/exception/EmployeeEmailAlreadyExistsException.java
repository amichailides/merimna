package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeEmailAlreadyExistsException extends BaseDomainException {
    public EmployeeEmailAlreadyExistsException(String email) {
        super(ErrorCode.EMPLOYEE_EMAIL_ALREADY_EXISTS, email);
    }
}
