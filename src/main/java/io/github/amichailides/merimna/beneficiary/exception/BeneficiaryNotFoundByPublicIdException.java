package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.UUID;

public class BeneficiaryNotFoundByPublicIdException extends BaseDomainException {

    public BeneficiaryNotFoundByPublicIdException(UUID publicId) {
        super(ErrorCode.BENEFICIARY_NOT_FOUND_BY_ID, publicId);
    }

}
