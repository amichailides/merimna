package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;

public class BeneficiaryAlreadyInactiveException extends BaseDomainException{
    public BeneficiaryAlreadyInactiveException() {
        super(ErrorCode.BENEFICIARY_ALREADY_INACTIVE);
    }
}
