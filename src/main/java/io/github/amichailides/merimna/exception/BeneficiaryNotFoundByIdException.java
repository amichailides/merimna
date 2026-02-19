package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class BeneficiaryNotFoundByIdException extends BaseBusinessException {

    public BeneficiaryNotFoundByIdException(Long beneficiaryId) {
        super(ErrorCode.BENEFICIARY_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                "beneficiary.notFoundById",
                beneficiaryId);
    }

}
