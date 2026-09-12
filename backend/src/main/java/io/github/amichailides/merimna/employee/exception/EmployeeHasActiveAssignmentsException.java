package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class EmployeeHasActiveAssignmentsException extends BaseDomainException {
    public EmployeeHasActiveAssignmentsException(UUID employeePublicId) {
        super(ErrorCode.EMPLOYEE_HAS_ACTIVE_ASSIGNMENTS, Map.of(
                "employeePublicId", employeePublicId
        ));
    }
}