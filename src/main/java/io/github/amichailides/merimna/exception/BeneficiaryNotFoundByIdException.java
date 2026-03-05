package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;

public class BeneficiaryNotFoundByIdException extends BaseDomainException {

    public BeneficiaryNotFoundByIdException(Long beneficiaryId) {
        super(ErrorCode.BENEFICIARY_NOT_FOUND_BY_ID, beneficiaryId);
    }

}
