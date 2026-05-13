package io.github.amichailides.merimna.allergy.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

import java.util.Map;
import java.util.UUID;

public class AllergyNotFoundException extends BaseApplicationException {
    public AllergyNotFoundException(UUID allergyPublicId) {
        super(ErrorCode.ALLERGY_NOT_FOUND, Map.of(
                "allergyPublicId", allergyPublicId
        ));
    }
}
