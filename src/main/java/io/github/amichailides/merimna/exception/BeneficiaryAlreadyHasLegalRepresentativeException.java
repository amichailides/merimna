package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;

public class BeneficiaryAlreadyHasLegalRepresentativeException extends BaseDomainException {
    public BeneficiaryAlreadyHasLegalRepresentativeException(Long beneficiaryId) {
        super(ErrorCode.BENEFICIARY_ALREADY_HAS_LEGAL_REPRESENTATIVE, beneficiaryId);
    }
}
