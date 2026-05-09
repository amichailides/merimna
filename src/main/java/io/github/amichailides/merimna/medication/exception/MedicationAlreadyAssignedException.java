package io.github.amichailides.merimna.medication.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class MedicationAlreadyAssignedException extends BaseDomainException {
    public MedicationAlreadyAssignedException(UUID currentBeneficiaryPublicId,
                                              UUID targetBeneficiaryPublicId) {
        super(ErrorCode.MEDICATION_ALREADY_ASSIGNED, Map.of(
                "currentBeneficiaryPublicId", currentBeneficiaryPublicId,
                "targetBeneficiaryPublicId", targetBeneficiaryPublicId
        ));
    }
}