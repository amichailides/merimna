package io.github.amichailides.merimna.allergy.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;
import lombok.Getter;

@Getter
public class AllergyAlreadyAssignedException extends BaseDomainException {


    public AllergyAlreadyAssignedException() {
        super(ErrorCode.ALLERGY_ALREADY_ASSIGNED);
    }
}
