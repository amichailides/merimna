package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;

public class BeneficiaryNotFoundByAmkaException extends BaseDomainException{

    public BeneficiaryNotFoundByAmkaException(String beneficiaryAmka) {
        super(ErrorCode.BENEFICIARY_NOT_FOUND_BY_AMKA, beneficiaryAmka);
    }

}
