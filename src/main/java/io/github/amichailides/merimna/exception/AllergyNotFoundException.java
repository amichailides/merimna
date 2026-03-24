package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;

public class AllergyNotFoundException extends BaseDomainException {
    public AllergyNotFoundException(Long allergyId) {

        super(ErrorCode.ALLERGY_NOT_FOUND, allergyId);
    }
}
