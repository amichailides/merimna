package io.github.amichailides.merimna.medication.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class MedicationNotOwnedByBeneficiaryException extends BaseDomainException {
    public MedicationNotOwnedByBeneficiaryException(Long medicationId, String beneficiaryPublicId) {
        super(ErrorCode.MEDICATION_NOT_OWNED_BY_BENEFICIARY,
                medicationId,
                beneficiaryPublicId);
    }
}
