package io.github.amichailides.merimna.placement.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class EmployeePlacementInvalidEndDate extends BaseDomainException {
    public EmployeePlacementInvalidEndDate() {
        super(ErrorCode.EMPLOYEE_PLACEMENT_INVALID_END_DATE);
    }
}
