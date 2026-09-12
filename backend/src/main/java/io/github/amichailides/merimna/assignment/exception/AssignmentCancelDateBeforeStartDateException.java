package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class AssignmentCancelDateBeforeStartDateException extends BaseDomainException {
    public AssignmentCancelDateBeforeStartDateException(UUID assignmentPublicId,
                                                        LocalDate startDate,
                                                        LocalDate cancelDate) {
        super(ErrorCode.ASSIGNMENT_CANCEL_DATE_BEFORE_START_DATE, Map.of(
                "assignmentPublicId", assignmentPublicId,
                "startDate", startDate,
                "cancelDate", cancelDate
        ));
    }
}
