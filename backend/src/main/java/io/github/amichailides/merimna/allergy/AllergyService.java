package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface AllergyService {
    AllergyReadOnlyDTO createAllergy(UUID beneficiaryPublicId, AllergyCreateDTO dto);

    AllergyReadOnlyDTO updateAllergy(UUID beneficiaryPublicId, UUID allergyPublicId, AllergyUpdateDTO dto);

    void deleteAllergy(UUID beneficiaryPublicId, UUID allergyPublicId);

    List<AllergyReadOnlyDTO> getAllergiesByBeneficiary(UUID beneficiaryPublicId);

    AllergyReadOnlyDTO getAllergyByPublicId(UUID beneficiaryPublicId, UUID allergyPublicId);
}
