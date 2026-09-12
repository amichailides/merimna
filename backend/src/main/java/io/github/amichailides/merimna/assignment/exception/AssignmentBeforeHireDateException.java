package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class AssignmentBeforeHireDateException extends BaseDomainException {
    public AssignmentBeforeHireDateException(
            UUID employeePublicId,
            LocalDate hireDate,
            LocalDate startDate) {
        super(ErrorCode.ASSIGNMENT_BEFORE_HIRE_DATE, Map.of(
                "employeePublicId", employeePublicId,
                "hireDate", hireDate,
                "startDate", startDate
        ));
    }
}
