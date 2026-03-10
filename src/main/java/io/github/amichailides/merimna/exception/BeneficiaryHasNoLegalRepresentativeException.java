package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;

public class BeneficiaryHasNoLegalRepresentativeException extends BaseDomainException {
    public BeneficiaryHasNoLegalRepresentativeException(Long beneficiaryId) {
        super(ErrorCode.BENEFICIARY_HAS_NO_LEGAL_REPRESENTATIVE, beneficiaryId);
    }
}
