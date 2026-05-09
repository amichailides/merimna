package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class EmployeeAlreadyActiveException extends BaseDomainException {
    public EmployeeAlreadyActiveException(UUID employeePublicId) {
        super(ErrorCode.EMPLOYEE_ALREADY_ACTIVE, Map.of(
                "employeePublicId", employeePublicId
        ));
    }
}
