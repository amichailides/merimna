package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AssignmentEndDateBeforeStartDateException extends BaseDomainException {
    public AssignmentEndDateBeforeStartDateException() {
        super(ErrorCode.ASSIGNMENT_END_DATE_BEFORE_START_DATE);
    }
}
