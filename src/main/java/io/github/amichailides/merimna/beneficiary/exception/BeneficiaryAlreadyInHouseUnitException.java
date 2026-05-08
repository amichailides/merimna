package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

import static io.github.amichailides.merimna.common.error.ErrorCode.BENEFICIARY_ALREADY_IN_HOUSE_UNIT;

public class BeneficiaryAlreadyInHouseUnitException extends BaseDomainException {

    public BeneficiaryAlreadyInHouseUnitException(UUID beneficiaryPublicId, UUID houseUnitPublicId) {
        super(BENEFICIARY_ALREADY_IN_HOUSE_UNIT, Map.of(
                "beneficiaryPublicId", beneficiaryPublicId,
                "houseUnitPublicId", houseUnitPublicId
        ));
    }
}
