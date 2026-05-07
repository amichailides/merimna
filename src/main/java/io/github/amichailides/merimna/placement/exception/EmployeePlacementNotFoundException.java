package io.github.amichailides.merimna.placement.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class EmployeePlacementNotFoundException extends BaseApplicationException {
    public EmployeePlacementNotFoundException() {
        super(ErrorCode.EMPLOYEE_PLACEMENT_NOT_FOUND);
    }
}
