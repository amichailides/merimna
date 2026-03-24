package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;

public class MedicationNotFound extends BaseDomainException {
    public MedicationNotFound(Long medicationId) {
        super(ErrorCode.MEDICATION_NOT_FOUND, medicationId);
    }
}
