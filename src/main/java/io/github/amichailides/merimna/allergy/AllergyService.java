package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;

import java.util.List;

public interface AllergyService {
    AllergyReadOnlyDTO createAllergy(String beneficiaryPublicId, AllergyCreateDTO dto);
    AllergyReadOnlyDTO updateAllergy(String beneficiaryPublicId, Long allergyId, AllergyUpdateDTO dto);
    void deleteAllergy(String beneficiaryPublicId, Long allergyId);
    List<AllergyReadOnlyDTO> getAllergiesByBeneficiary(String beneficiaryPublicId);
    AllergyReadOnlyDTO getAllergyById(String beneficiaryPublicId, Long allergyId);
}
