package io.github.amichailides.merimna.placement.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeePlacementNotFoundException extends BaseDomainException {
    public EmployeePlacementNotFoundException() {
        super(ErrorCode.EMPLOYEE_PLACEMENT_NOT_FOUND);
    }
}
