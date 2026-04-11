package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AssignmentCancelDateBeforeStartDateException extends BaseDomainException {
    public AssignmentCancelDateBeforeStartDateException() {
        super(ErrorCode.ASSIGNMENT_CANCEL_DATE_BEFORE_START_DATE);
    }
}
