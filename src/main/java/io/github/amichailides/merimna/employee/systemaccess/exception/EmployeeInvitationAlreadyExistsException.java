package io.github.amichailides.merimna.employee.systemaccess.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeInvitationAlreadyExistsException
        extends BaseDomainException {

    public EmployeeInvitationAlreadyExistsException() {
        super(ErrorCode.EMPLOYEE_INVITATION_ALREADY_EXISTS);
    }
}