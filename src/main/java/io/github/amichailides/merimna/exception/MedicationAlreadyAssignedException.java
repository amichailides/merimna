package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;

public class MedicationAlreadyAssignedException extends BaseDomainException {
    public MedicationAlreadyAssignedException() {
        super(ErrorCode.MEDICATION_ALREADY_ASSIGNED);
    }
}