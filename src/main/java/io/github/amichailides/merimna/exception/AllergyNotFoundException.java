package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class AllergyNotFoundException extends BaseBusinessException {
    public AllergyNotFoundException(Long id) {

        super(ErrorCode.ALLERGY_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                ErrorCode.ALLERGY_NOT_FOUND.getMessageKey(),
                id);
    }
}
