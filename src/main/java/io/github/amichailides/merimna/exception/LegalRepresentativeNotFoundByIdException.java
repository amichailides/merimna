package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;

public class LegalRepresentativeNotFoundByIdException extends BaseDomainException {
    public LegalRepresentativeNotFoundByIdException(Long id)
    {
        super(ErrorCode.LEGAL_REPRESENTATIVE_NOT_FOUND_BY_ID, id);
    }
}
