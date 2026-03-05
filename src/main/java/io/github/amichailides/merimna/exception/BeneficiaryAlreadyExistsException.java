package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;

public class BeneficiaryAlreadyExistsException extends BaseDomainException{

    public BeneficiaryAlreadyExistsException(String amka) {
        super(ErrorCode.AMKA_ALREADY_EXISTS, amka);
    }
}
