package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.Allergy;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.exception.ConflictValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AllergyValidator {

    private final AllergyRepository allergyRepository;

    public void validateCreate(Beneficiary beneficiary, Allergy allergy) {
        validateBeneficiaryIsActive(beneficiary);
        validateDuplicateSubstanceOnCreate(beneficiary, allergy.getSubstance());
    }

    public void validateForUpdate(Allergy allergy, AllergyUpdateDTO dto) {
        allergy.getBeneficiary().ensureActive();
        validateDuplicateSubstanceOnUpdate(allergy, dto);

    }


    private void validateDuplicateSubstanceOnUpdate(Allergy allergy, AllergyUpdateDTO dto) {
        if (dto.substance() == null || dto.substance().isBlank()) {
            return;
        }

        boolean exists = allergyRepository
                .existsByBeneficiaryIdAndSubstanceIgnoreCaseAndIdNot(
                        allergy.getBeneficiary().getId(),
                        dto.substance(),
                        allergy.getId()
                );

        if (exists) {
            throw substanceConflict();
        }
    }

    private void validateBeneficiaryIsActive(Beneficiary beneficiary) {
        beneficiary.ensureActive();
    }

    private void validateDuplicateSubstanceOnCreate(Beneficiary beneficiary, String substance) {
        if (beneficiary.getId() == null || substance == null || substance.isBlank()) {
            return;
        }

        boolean exists = allergyRepository
                .existsByBeneficiaryIdAndSubstanceIgnoreCase(beneficiary.getId(), substance);

        if (exists) {
            throw substanceConflict();
        }
    }

    private ConflictValidationException substanceConflict() {
        Map<String, String> conflicts = new LinkedHashMap<>();
        conflicts.put("substance", ErrorCode.ALLERGY_DUPLICATE_SUBSTANCE.getMessageKey());
        return new ConflictValidationException(conflicts);
    }
}
