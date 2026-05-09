package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class AssignmentEndDateBeforeStartDateException extends BaseDomainException {
    public AssignmentEndDateBeforeStartDateException(UUID assignmentPublicId,
                                                     LocalDate startDate,
                                                     LocalDate endDate) {
        super(ErrorCode.ASSIGNMENT_END_DATE_BEFORE_START_DATE, Map.of(
                "assignmentPublicId", assignmentPublicId,
                "startDate", startDate,
                "endDate", endDate
        ));
    }
}
