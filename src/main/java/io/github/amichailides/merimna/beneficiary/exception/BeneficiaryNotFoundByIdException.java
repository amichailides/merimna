package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class BeneficiaryNotFoundByIdException extends BaseDomainException {

    public BeneficiaryNotFoundByIdException(Long beneficiaryId) {
        super(ErrorCode.BENEFICIARY_NOT_FOUND_BY_ID, beneficiaryId);
    }

}
