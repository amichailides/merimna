package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;

public class LegalRepresentativeAlreadyAssignedException extends BaseDomainException {
    public LegalRepresentativeAlreadyAssignedException(Long legalRepresentativeId, Long beneficiaryId) {
        super(ErrorCode.LEGAL_REPRESENTATIVE_ALREADY_ASSIGNED, legalRepresentativeId, beneficiaryId);
    }
}
