package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;

import java.util.List;

public interface AllergyService {
    AllergyReadOnlyDTO addAllergy(Long id, AllergyCreateDTO dto);
    AllergyReadOnlyDTO updateAllergy(Long beneficiaryId, Long allergyId, AllergyUpdateDTO dto);
    void deleteAllergy(Long beneficiaryId, Long allergyId);
    List<AllergyReadOnlyDTO> getAllergiesByBeneficiary(Long beneficiaryId);
    AllergyReadOnlyDTO getAllergy(Long beneficiaryId, Long allergyId);
}
