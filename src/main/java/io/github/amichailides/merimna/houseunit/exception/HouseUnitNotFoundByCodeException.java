package io.github.amichailides.merimna.houseunit.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class HouseUnitNotFoundByCodeException extends BaseDomainException {
    public HouseUnitNotFoundByCodeException(String code) {
        super(ErrorCode.HOUSE_UNIT_NOT_FOUND_BY_CODE, code);
    }
}
