package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class InvalidAssignmentDateRangeException extends BaseDomainException {
    public InvalidAssignmentDateRangeException() {
        super(ErrorCode.ASSIGNMENT_INVALID_DATE_RANGE);
    }
}
