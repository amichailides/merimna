package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.DomainValidationException;
import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationDiscontinueDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Encapsulates medication-specific business validation that cannot be expressed
 * through DTO-level Bean Validation alone.
 */
@Component
public class MedicationValidator {

    public void validateForCreate(MedicationCreateDTO dto) {
        Map<String, String> errors = new LinkedHashMap<>();

        validateStartedAt(dto.startedAt(), errors);

        throwIfErrors(errors);
    }

    public void validateForDiscontinue(MedicationDiscontinueDTO dto) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (dto.endedAt() != null && dto.endedAt().isAfter(LocalDate.now())) {
            errors.put(
                    "endedAt",
                    ErrorCode.MEDICATION_END_DATE_IN_FUTURE.getMessageKey()
            );
        }

        throwIfErrors(errors);
    }

    public void validateForUpdate(MedicationUpdateDTO dto) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (dto.startedAt() != null) {
            validateStartedAt(dto.startedAt(), errors);
        }

        throwIfErrors(errors);
    }

    private void validateStartedAt(
            LocalDate startedAt,
            Map<String, String> errors
    ) {
        if (startedAt != null && startedAt.isAfter(LocalDate.now())) {
            errors.put(
                    "startedAt",
                    ErrorCode.MEDICATION_START_DATE_IN_FUTURE.getMessageKey()
            );
        }
    }

    private void throwIfErrors(Map<String, String> errors) {
        if (!errors.isEmpty()) {
            throw new DomainValidationException(errors);
        }
    }
}