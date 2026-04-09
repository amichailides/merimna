package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AssignmentNotFoundException extends BaseDomainException {
    public AssignmentNotFoundException(Long assignmentId) {
        super(ErrorCode.ASSIGNMENT_NOT_FOUND, assignmentId);
    }
}
