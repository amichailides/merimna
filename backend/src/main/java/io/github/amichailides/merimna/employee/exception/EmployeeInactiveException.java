package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class EmployeeInactiveException extends BaseDomainException {
    public EmployeeInactiveException(UUID employeePublicId) {
        super(ErrorCode.EMPLOYEE_INACTIVE, Map.of(
                "employeePublicId", employeePublicId
        ));
    }
}
