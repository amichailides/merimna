package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class EmployeeEmailAlreadyExistsException extends BaseApplicationException {
    public EmployeeEmailAlreadyExistsException(String email) {
        super(ErrorCode.EMPLOYEE_EMAIL_ALREADY_EXISTS, email);
    }
}
