package io.github.amichailides.merimna.houseunit.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

import java.util.UUID;

public class HouseUnitNotFoundException extends BaseApplicationException {
    public HouseUnitNotFoundException(UUID publicId) {
        super(ErrorCode.HOUSE_UNIT_NOT_FOUND, publicId);
    }
}
