package io.github.amichailides.merimna.houseunit.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class HouseUnitFullException extends BaseDomainException {
    public HouseUnitFullException(String code, long currentCount) {
        super(ErrorCode.HOUSE_UNIT_FULL, code, currentCount);
    }
}
