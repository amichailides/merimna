package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface AllergyService {
    AllergyReadOnlyDTO createAllergy(UUID beneficiaryPublicId, AllergyCreateDTO dto);
    AllergyReadOnlyDTO updateAllergy(UUID beneficiaryPublicId, Long allergyId, AllergyUpdateDTO dto);
    void deleteAllergy(UUID beneficiaryPublicId, Long allergyId);
    List<AllergyReadOnlyDTO> getAllergiesByBeneficiary(UUID beneficiaryPublicId);
    AllergyReadOnlyDTO getAllergyById(UUID beneficiaryPublicId, Long allergyId);
}
