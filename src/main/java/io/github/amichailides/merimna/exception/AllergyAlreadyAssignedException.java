package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import lombok.Getter;

@Getter
public class AllergyAlreadyAssignedException extends BaseDomainException {


    public AllergyAlreadyAssignedException() {
        super(ErrorCode.ALLERGY_ALREADY_ASSIGNED);
    }
}
