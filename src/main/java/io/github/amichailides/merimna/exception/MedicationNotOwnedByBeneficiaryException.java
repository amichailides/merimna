package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;

public class MedicationNotOwnedByBeneficiaryException extends BaseDomainException {
    public MedicationNotOwnedByBeneficiaryException(Long medicationId, Long beneficiaryId) {
        super(ErrorCode.MEDICATION_NOT_OWNED_BY_BENEFICIARY,
                medicationId,
                beneficiaryId);
    }
}
