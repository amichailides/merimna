package io.github.amichailides.merimna.legalrepresentative.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class LegalRepresentativeNotFoundByIdException extends BaseDomainException {
    public LegalRepresentativeNotFoundByIdException(Long id)
    {
        super(ErrorCode.LEGAL_REPRESENTATIVE_NOT_FOUND_BY_ID, id);
    }
}
