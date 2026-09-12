package io.github.amichailides.merimna.medication.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

import java.util.Map;
import java.util.UUID;

public class MedicationNotFoundException extends BaseApplicationException {
    public MedicationNotFoundException(UUID medicationPublicId) {
        super(ErrorCode.MEDICATION_NOT_FOUND, Map.of(
                "medicationPublicId", medicationPublicId
        ));
    }
}
