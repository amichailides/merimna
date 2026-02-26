package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class BeneficiaryAlreadyExistsException extends BaseBusinessException{

    public BeneficiaryAlreadyExistsException(String amka) {
        super(ErrorCode.AMKA_ALREADY_EXISTS,
                HttpStatus.CONFLICT,
                "beneficiary.amkaAlreadyExists",
                amka);
    }
}
