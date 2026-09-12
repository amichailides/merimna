package io.github.amichailides.merimna.legalrepresentative.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

import java.util.Map;

public class LegalRepresentativeNotFoundByIdException extends BaseApplicationException {
    public LegalRepresentativeNotFoundByIdException(Long legalRepresentativeId) {
        super(ErrorCode.LEGAL_REPRESENTATIVE_NOT_FOUND_BY_ID, Map.of(
                "legalRepresentativeId", legalRepresentativeId
        ));
    }
}
