package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class AssignmentOverlapNotAllowedException extends BaseDomainException {
    public AssignmentOverlapNotAllowedException(
            UUID employeePublicId,
            LocalDate startDate,
            LocalDate endDate) {
        super(ErrorCode.ASSIGNMENT_OVERLAP_NOT_ALLOWED, buildContext(employeePublicId, startDate, endDate));
    }

    private static Map<String, Object> buildContext(
            UUID employeePublicId,
            LocalDate startDate,
            LocalDate endDate) {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("employeePublicId", employeePublicId);
        context.put("startDate", startDate);

        if (endDate != null) {
            context.put("endDate", endDate);
        }

        return context;
    }
}
