package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeeHasActiveAssignmentsException extends BaseDomainException {
    public EmployeeHasActiveAssignmentsException() {
        super(ErrorCode.EMPLOYEE_HAS_ACTIVE_ASSIGNMENTS);
    }
}
