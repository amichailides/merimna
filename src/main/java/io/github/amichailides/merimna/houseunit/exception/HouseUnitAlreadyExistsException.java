package io.github.amichailides.merimna.houseunit.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class HouseUnitAlreadyExistsException extends BaseDomainException {
    public HouseUnitAlreadyExistsException(String code) {
        super(ErrorCode.HOUSE_UNIT_ALREADY_EXISTS, code);
    }
}
