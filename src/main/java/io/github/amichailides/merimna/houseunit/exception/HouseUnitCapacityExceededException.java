package io.github.amichailides.merimna.houseunit.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class HouseUnitCapacityExceededException extends BaseDomainException {
    public HouseUnitCapacityExceededException(String code) {
        super(ErrorCode.HOUSE_UNIT_CAPACITY_EXCEEDED, code);
    }
}
