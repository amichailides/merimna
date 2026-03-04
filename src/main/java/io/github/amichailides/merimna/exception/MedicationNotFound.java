package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class MedicationNotFound extends BaseBusinessException {
    public MedicationNotFound(Long medicationId) {
        super(ErrorCode.MEDICATION_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                ErrorCode.MEDICATION_NOT_FOUND.getMessageKey(),
                medicationId);
    }
}
