package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class BeneficiaryAlreadyInactiveException extends BaseBusinessException{
    public BeneficiaryAlreadyInactiveException() {
        super(
                ErrorCode.BENEFICIARY_ALREADY_INACTIVE,
                HttpStatus.CONFLICT,
                ErrorCode.BENEFICIARY_ALREADY_INACTIVE.getMessageKey()
        );
    }
}
