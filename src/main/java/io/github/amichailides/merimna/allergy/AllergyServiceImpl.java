package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.allergy.exception.AllergyNotFoundException;
import io.github.amichailides.merimna.allergy.exception.AllergyNotOwnedByBeneficiaryException;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.domain.Allergy;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AllergyServiceImpl implements AllergyService {
    private final AllergyRepository allergyRepository;
    private final AllergyMapper allergyMapper;
    private final BeneficiaryRepository beneficiaryRepository;

    @Override
    @Transactional
    public AllergyReadOnlyDTO createAllergy(Long beneficiaryId, AllergyCreateDTO dto) {

        // TODO(#12): Add AllergyValidator rules (duplicates, severity checks)

        Beneficiary existing = getBeneficiaryOrThrow(beneficiaryId);

        Allergy allergy = allergyMapper.toEntity(dto);
        existing.addAllergy(allergy);
        Allergy savedAllergy = allergyRepository.save(allergy);

        return allergyMapper.toDTO(savedAllergy);
    }

    @Override
    @Transactional
    public AllergyReadOnlyDTO updateAllergy(Long beneficiaryId, Long allergyId, AllergyUpdateDTO dto) {

        // TODO: Add AllergyValidator for allergy business rules on update

        Allergy allergy = getAllergyOrThrow(allergyId, beneficiaryId);

        allergyMapper.updateEntity(allergy, dto);

        return allergyMapper.toDTO(allergy);
    }

    @Override
    @Transactional
    public void deleteAllergy(Long beneficiaryId, Long allergyId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);

        Allergy allergy = getAllergyOrThrow(allergyId, beneficiaryId);

        beneficiary.removeAllergy(allergy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllergyReadOnlyDTO> getAllergiesByBeneficiary(Long beneficiaryId) {
        if (!beneficiaryRepository.existsById(beneficiaryId)) {
            throw new BeneficiaryNotFoundByIdException(beneficiaryId);
        }

        return allergyRepository.findAllByBeneficiaryId(beneficiaryId)
                .stream()
                .map(allergyMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AllergyReadOnlyDTO getAllergyById(Long beneficiaryId, Long allergyId) {
        return allergyMapper.toDTO(getAllergyOrThrow(allergyId, beneficiaryId));
    }

    private Beneficiary getBeneficiaryOrThrow(Long beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));
    }

    private Allergy getAllergyOrThrow(Long allergyId, Long beneficiaryId) {
        return allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)
                .orElseThrow(() -> {
                    if (!allergyRepository.existsById(allergyId)) {
                        return new AllergyNotFoundException(allergyId);
                    }
                    return new AllergyNotOwnedByBeneficiaryException(allergyId, beneficiaryId);
                });
    }
}
