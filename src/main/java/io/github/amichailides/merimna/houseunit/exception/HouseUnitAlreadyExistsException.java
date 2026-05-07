package io.github.amichailides.merimna.houseunit.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class HouseUnitAlreadyExistsException extends BaseApplicationException {
    public HouseUnitAlreadyExistsException(String code) {

        super(ErrorCode.HOUSE_UNIT_ALREADY_EXISTS, code);
    }
}
