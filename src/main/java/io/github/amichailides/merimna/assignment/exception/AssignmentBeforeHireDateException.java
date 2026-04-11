package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AssignmentBeforeHireDateException extends BaseDomainException {
    public AssignmentBeforeHireDateException() {
        super(ErrorCode.ASSIGNMENT_BEFORE_HIRE_DATE);
    }
}
