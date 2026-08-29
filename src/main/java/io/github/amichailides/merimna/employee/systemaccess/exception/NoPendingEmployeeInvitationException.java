package io.github.amichailides.merimna.employee.systemaccess.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.UUID;

public class NoPendingEmployeeInvitationException
        extends BaseDomainException {

    public NoPendingEmployeeInvitationException(UUID employeePublicId) {
        super(
                ErrorCode.NO_PENDING_EMPLOYEE_INVITATION,
                employeePublicId
        );
    }
}