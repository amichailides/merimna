package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.allergy.exception.DuplicateAllergyException;
import io.github.amichailides.merimna.domain.Allergy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AllergyValidator {

    private final AllergyRepository allergyRepository;

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
            throw new DuplicateAllergyException(dto.substance());
        }
    }
}
