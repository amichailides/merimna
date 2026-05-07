package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class BeneficiaryAlreadyExistsException extends BaseApplicationException {

    public BeneficiaryAlreadyExistsException(String amka) {
        super(ErrorCode.AMKA_ALREADY_EXISTS, amka);
    }
}
