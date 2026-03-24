package io.github.amichailides.merimna.legalrepresentative.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class LegalRepresentativeAlreadyAssignedException extends BaseDomainException {
    public LegalRepresentativeAlreadyAssignedException(Long legalRepresentativeId, Long beneficiaryId) {
        super(ErrorCode.LEGAL_REPRESENTATIVE_ALREADY_ASSIGNED, legalRepresentativeId, beneficiaryId);
    }
}
