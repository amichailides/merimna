package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AssignmentOverlapNotAllowedException extends BaseDomainException {
    public AssignmentOverlapNotAllowedException() {
        super(ErrorCode.ASSIGNMENT_OVERLAP_NOT_ALLOWED);
    }
}
