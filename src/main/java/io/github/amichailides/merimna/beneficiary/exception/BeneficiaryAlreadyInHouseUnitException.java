package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.exception.BaseDomainException;

import static io.github.amichailides.merimna.common.error.ErrorCode.BENEFICIARY_ALREADY_IN_HOUSE_UNIT;

public class BeneficiaryAlreadyInHouseUnitException extends BaseDomainException {
    public BeneficiaryAlreadyInHouseUnitException(Long beneficiaryId, String houseUnitCode) {

        super(BENEFICIARY_ALREADY_IN_HOUSE_UNIT, beneficiaryId, houseUnitCode);
    }
}
