package io.github.amichailides.merimna.legalrepresentative.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class LegalRepresentativeNotAssignedException extends BaseDomainException {
    public LegalRepresentativeNotAssignedException(Long legalRepresentativeId, Long beneficiaryId) {
        super(ErrorCode.LEGAL_REPRESENTATIVE_NOT_ASSIGNED,
                legalRepresentativeId,
                beneficiaryId);
    }
}
