package io.github.amichailides.merimna.placement.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeePlacementAlreadyClosed extends BaseDomainException {
    public EmployeePlacementAlreadyClosed() {
        super(ErrorCode.EMPLOYEE_PLACEMENT_ALREADY_CLOSED);
    }
}
