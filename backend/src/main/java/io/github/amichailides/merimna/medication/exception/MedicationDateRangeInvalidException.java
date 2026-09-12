package io.github.amichailides.merimna.medication.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class MedicationDateRangeInvalidException extends BaseDomainException {

    public MedicationDateRangeInvalidException(
            UUID medicationPublicId,
            LocalDate startedAt,
            LocalDate endedAt
    ) {
        super(
                ErrorCode.MEDICATION_DATE_RANGE_INVALID,
                Map.of(
                        "medicationPublicId", medicationPublicId,
                        "startedAt", startedAt,
                        "endedAt", endedAt
                )
        );
    }
}
