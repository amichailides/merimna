package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.dto.AllergyUpdateDTO;

import java.util.List;

public interface AllergiesService {
    AllergyReadOnlyDTO addAllergy(Long id, AllergyCreateDTO dto);
    AllergyReadOnlyDTO updateAllergy(Long beneficiaryId, Long allergyId, AllergyUpdateDTO dto);
    void deleteAllergy(Long beneficiaryId, Long allergyId);
    public List<AllergyReadOnlyDTO> getAllergiesByBeneficiary(Long beneficiaryId);
}
