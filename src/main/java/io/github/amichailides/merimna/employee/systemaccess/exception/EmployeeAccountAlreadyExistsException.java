package io.github.amichailides.merimna.employee.systemaccess.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.UUID;

public class EmployeeAccountAlreadyExistsException
        extends BaseDomainException {

    public EmployeeAccountAlreadyExistsException(UUID employeePublicId) {
        super(
                ErrorCode.EMPLOYEE_ALREADY_HAS_ACCOUNT,
                employeePublicId
        );
    }
}