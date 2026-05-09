package io.github.amichailides.merimna.medication.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

import java.util.Map;

public class MedicationNotFoundException extends BaseApplicationException {
    public MedicationNotFoundException(Long medicationId) {
        super(ErrorCode.MEDICATION_NOT_FOUND, Map.of(
                "medicationId", medicationId
        ));
    }
}
