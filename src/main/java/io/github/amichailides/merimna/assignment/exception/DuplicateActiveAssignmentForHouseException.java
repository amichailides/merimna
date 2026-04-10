package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class DuplicateActiveAssignmentForHouseException extends BaseDomainException {
    public DuplicateActiveAssignmentForHouseException() {
        super(ErrorCode.ASSIGNMENT_DUPLICATE_ACTIVE_FOR_HOUSE);
    }
}
