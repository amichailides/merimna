package io.github.amichailides.merimna.medication.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class MedicationNotOwnedByBeneficiaryException extends BaseDomainException {
    public MedicationNotOwnedByBeneficiaryException(UUID medicationPublicId, UUID beneficiaryPublicId) {
        super(ErrorCode.MEDICATION_NOT_OWNED_BY_BENEFICIARY, Map.of(
                "medicationPublicId", medicationPublicId,
                "beneficiaryPublicId", beneficiaryPublicId
        ));
    }
}
