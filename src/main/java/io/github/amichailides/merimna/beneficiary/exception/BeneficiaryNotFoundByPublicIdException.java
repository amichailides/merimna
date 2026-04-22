package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class BeneficiaryNotFoundByPublicIdException extends BaseDomainException {

    public BeneficiaryNotFoundByPublicIdException(String publicId) {
        super(ErrorCode.BENEFICIARY_NOT_FOUND_BY_ID, publicId);
    }

}
