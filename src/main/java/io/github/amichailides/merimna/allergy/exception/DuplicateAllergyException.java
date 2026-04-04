package io.github.amichailides.merimna.allergy.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class DuplicateAllergyException extends BaseDomainException {
    public DuplicateAllergyException(String substance) {
        super(ErrorCode.ALLERGY_DUPLICATE_SUBSTANCE, substance);
    }
}
