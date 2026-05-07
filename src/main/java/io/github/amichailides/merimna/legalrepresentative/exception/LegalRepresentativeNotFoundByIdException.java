package io.github.amichailides.merimna.legalrepresentative.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class LegalRepresentativeNotFoundByIdException extends BaseApplicationException {
    public LegalRepresentativeNotFoundByIdException(Long id)
    {
        super(ErrorCode.LEGAL_REPRESENTATIVE_NOT_FOUND_BY_ID, id);
    }
}
