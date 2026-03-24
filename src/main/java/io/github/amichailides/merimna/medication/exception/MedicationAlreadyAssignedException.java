package io.github.amichailides.merimna.medication.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class MedicationAlreadyAssignedException extends BaseDomainException {
    public MedicationAlreadyAssignedException() {
        super(ErrorCode.MEDICATION_ALREADY_ASSIGNED);
    }
}