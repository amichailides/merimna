package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class BeneficiaryNotFoundByAmkaException extends BaseApplicationException {

    public BeneficiaryNotFoundByAmkaException(String beneficiaryAmka) {
        super(ErrorCode.BENEFICIARY_NOT_FOUND_BY_AMKA, beneficiaryAmka);
    }

}
