package io.github.amichailides.merimna.placement.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class EmployeePlacementOverlapException extends BaseDomainException {
    public EmployeePlacementOverlapException(UUID employeePublicId,
                                             LocalDate startDate,
                                             LocalDate endDate) {
        super(ErrorCode.EMPLOYEE_PLACEMENT_OVERLAP,
                buildContext(employeePublicId, startDate, endDate));
    }

    private static Map<String, Object> buildContext(UUID employeePublicId,
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
