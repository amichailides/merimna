package io.github.amichailides.merimna.houseunit.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class HouseUnitCapacityExceededException extends BaseDomainException {
    public HouseUnitCapacityExceededException(UUID houseUnitPublicId, long currentCount) {
        super(ErrorCode.HOUSE_UNIT_CAPACITY_EXCEEDED, Map.of(
                "houseUnitPublicId", houseUnitPublicId,
                "currentCount", currentCount
        ));
    }
}
