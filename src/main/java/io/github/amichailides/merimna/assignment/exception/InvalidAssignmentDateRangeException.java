package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;
import java.util.Map;

public class InvalidAssignmentDateRangeException extends BaseDomainException {
    public InvalidAssignmentDateRangeException(LocalDate startDate, LocalDate endDate) {
        super(ErrorCode.ASSIGNMENT_INVALID_DATE_RANGE, Map.of(
                "startDate", startDate,
                "endDate", endDate
        ));
    }
}
