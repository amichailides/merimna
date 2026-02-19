package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class BeneficiaryNotFoundByAmkaException extends BaseBusinessException{

    public BeneficiaryNotFoundByAmkaException(String beneficiaryAmka) {
        super(ErrorCode.BENEFICIARY_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                "beneficiary.notFoundByAmka",
                beneficiaryAmka);
    }

}
