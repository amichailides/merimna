package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AssignmentCancellationNotAllowedException extends BaseDomainException {
    public AssignmentCancellationNotAllowedException() {
        super(ErrorCode.ASSIGNMENT_CANCELLATION_NOT_ALLOWED);
    }
}
