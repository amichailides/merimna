package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.assignment.EmployeeAssignmentStatus;
import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class AssignmentCancellationNotAllowedException extends BaseDomainException {
    public AssignmentCancellationNotAllowedException(UUID assignmentPublicId,
                                                     EmployeeAssignmentStatus status) {

        super(ErrorCode.ASSIGNMENT_CANCELLATION_NOT_ALLOWED, Map.of(
                "assignmentPublicId", assignmentPublicId,
                "status", status
        ));
    }
}
