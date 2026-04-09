package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AssignmentAlreadyCancelledException extends BaseDomainException {
    public AssignmentAlreadyCancelledException(Long id) {
        super(ErrorCode.ASSIGNMENT_ALREADY_CANCELLED, id);
    }
}
