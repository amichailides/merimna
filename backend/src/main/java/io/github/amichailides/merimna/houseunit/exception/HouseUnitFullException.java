package io.github.amichailides.merimna.houseunit.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class HouseUnitFullException extends BaseDomainException {
    public HouseUnitFullException(UUID houseUnitPublicId, long currentCount) {
        super(ErrorCode.HOUSE_UNIT_FULL, Map.of(
                "houseUnitPublicId", houseUnitPublicId,
                "currentCount", currentCount
        ));
    }
}
