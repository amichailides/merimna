package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.UUID;

public class AssignmentNotFoundException extends BaseDomainException {
    public AssignmentNotFoundException(UUID assignmentPublicId) {
        super(ErrorCode.ASSIGNMENT_NOT_FOUND, assignmentPublicId);
    }
}
