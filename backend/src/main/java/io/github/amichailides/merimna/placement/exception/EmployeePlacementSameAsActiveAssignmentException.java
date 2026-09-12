package io.github.amichailides.merimna.placement.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class EmployeePlacementSameAsActiveAssignmentException extends BaseDomainException {

    public EmployeePlacementSameAsActiveAssignmentException(
            UUID employeePublicId,
            UUID houseUnitPublicId
    ) {
        super(
                ErrorCode.EMPLOYEE_PLACEMENT_SAME_AS_ACTIVE_ASSIGNMENT,
                buildContext(employeePublicId, houseUnitPublicId)
        );
    }

    private static Map<String, Object> buildContext(
            UUID employeePublicId,
            UUID houseUnitPublicId
    ) {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("employeePublicId", employeePublicId);
        context.put("houseUnitPublicId", houseUnitPublicId);
        return context;
    }
}