package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

import java.util.UUID;

public class BeneficiaryNotFoundByPublicIdException extends BaseApplicationException {

    public BeneficiaryNotFoundByPublicIdException(UUID publicId) {
        super(ErrorCode.BENEFICIARY_NOT_FOUND_BY_ID, publicId);
    }

}
