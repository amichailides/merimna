package io.github.amichailides.merimna.employee.systemaccess.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class NoPendingEmployeeInvitationException extends BaseDomainException {

    public NoPendingEmployeeInvitationException() {
        super(ErrorCode.NO_PENDING_EMPLOYEE_INVITATION);
    }
}