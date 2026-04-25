package io.github.amichailides.merimna.placement.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeePlacementOverlapException extends BaseDomainException {
    public EmployeePlacementOverlapException() {
        super(ErrorCode.EMPLOYEE_PLACEMENT_OVERLAP);
    }
}
