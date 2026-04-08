package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AssignmentPolicyViolationException extends BaseDomainException {
    public AssignmentPolicyViolationException() {

        super(ErrorCode.ASSIGNMENT_POLICY_VIOLATION);
    }
}
