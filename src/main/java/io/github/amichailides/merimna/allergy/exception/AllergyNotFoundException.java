package io.github.amichailides.merimna.allergy.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AllergyNotFoundException extends BaseDomainException {
    public AllergyNotFoundException(Long allergyId) {

        super(ErrorCode.ALLERGY_NOT_FOUND, allergyId);
    }
}
