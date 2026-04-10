package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AssignmentNotActiveException extends BaseDomainException {
    public AssignmentNotActiveException() {
        super(ErrorCode.ASSIGNMENT_NOT_ACTIVE);
    }
}
