package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.exception.AllergyNotFoundException;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.mapper.AllergyMapper;
import io.github.amichailides.merimna.model.Allergy;
import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.repository.AllergyRepository;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AllergiesServiceImpl implements AllergiesService{
    private final AllergyRepository allergyRepository;
    private final AllergyMapper allergyMapper;
    private final BeneficiaryRepository beneficiaryRepository;

    @Override
    @Transactional
    public AllergyReadOnlyDTO addAllergy(Long id, AllergyCreateDTO dto) {

        Beneficiary existing = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(id));

        Allergy allergy = allergyMapper.toEntity(dto);
        existing.addAllergy(allergy);
        Allergy savedAllergy = allergyRepository.save(allergy);

        return allergyMapper.toDTO(savedAllergy);
    }

    @Override
    @Transactional
    public AllergyReadOnlyDTO updateAllergy(Long beneficiaryId, Long allergyId, AllergyUpdateDTO dto) {
        Beneficiary existing = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));

        Allergy allergy = existing.getAllergies().stream()
                .filter(a -> a.getId().equals(allergyId))
                .findFirst()
                .orElseThrow( () -> new AllergyNotFoundException(allergyId));

        allergyMapper.updateEntity(dto, allergy);
        // dirty checking → αυτόματο UPDATE στο commit
        return allergyMapper.toDTO(allergy);
    }

    @Override
    @Transactional
    public void deleteAllergy(Long beneficiaryId, Long allergyId) {
        Beneficiary existing = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));

        Allergy allergy = existing.getAllergies().stream()
                .filter(a -> a.getId().equals(allergyId))
                .findFirst()
                .orElseThrow(() -> new AllergyNotFoundException(allergyId));

        // orphanRemoval=true → Hibernate τη σβήνει αυτόματα
        existing.removeAllergy(allergy);
    }
}
