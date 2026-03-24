package io.github.amichailides.merimna.medication.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class MedicationNotFoundException extends BaseDomainException {
    public MedicationNotFoundException(Long medicationId) {
        super(ErrorCode.MEDICATION_NOT_FOUND, medicationId);
    }
}
