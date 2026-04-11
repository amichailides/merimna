package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AssignmentTerminationNotAllowedException extends BaseDomainException {
    public AssignmentTerminationNotAllowedException() {
        super(ErrorCode.ASSIGNMENT_TERMINATION_NOT_ALLOWED);
    }
}
