package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.allergy.exception.AllergyNotFoundException;
import io.github.amichailides.merimna.allergy.exception.AllergyNotOwnedByBeneficiaryException;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByPublicIdException;
import io.github.amichailides.merimna.domain.Allergy;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AllergyServiceImpl implements AllergyService {
    private final AllergyRepository allergyRepository;
    private final AllergyMapper allergyMapper;
    private final BeneficiaryRepository beneficiaryRepository;
    private final AllergyValidator allergyValidator;

    @Override
    @Transactional
    public AllergyReadOnlyDTO createAllergy(UUID beneficiaryPublicId, AllergyCreateDTO dto) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryPublicId);

        Allergy allergy = allergyMapper.toEntity(dto);
        allergyValidator.validateCreate(beneficiary, allergy);

        beneficiary.addAllergy(allergy);
        Allergy savedAllergy = allergyRepository.save(allergy);

        return allergyMapper.toDTO(savedAllergy);
    }

    @Override
    @Transactional
    public AllergyReadOnlyDTO updateAllergy(UUID beneficiaryPublicId, UUID allergyPublicId, AllergyUpdateDTO dto) {
        Allergy allergy = getAllergyOrThrow(allergyPublicId, beneficiaryPublicId);

        allergyValidator.validateForUpdate(allergy, dto);
        allergyMapper.updateEntity(allergy, dto);

        return allergyMapper.toDTO(allergy);
    }

    @Override
    @Transactional
    public void deleteAllergy(UUID beneficiaryPublicId, UUID allergyPublicId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryPublicId);

        Allergy allergy = getAllergyOrThrow(allergyPublicId, beneficiaryPublicId);

        beneficiary.removeAllergy(allergy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllergyReadOnlyDTO> getAllergiesByBeneficiary(UUID beneficiaryPublicId) {
        if (!beneficiaryRepository.existsByPublicId(beneficiaryPublicId)) {
            throw new BeneficiaryNotFoundByPublicIdException(beneficiaryPublicId);
        }

        // TODO(#19): Consider projection-based query to avoid loading full Allergy entities for read operations
        return allergyRepository.findAllByBeneficiaryPublicId(beneficiaryPublicId)
                .stream()
                .map(allergyMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AllergyReadOnlyDTO getAllergyByPublicId(UUID beneficiaryPublicId, UUID allergyPublicId) {
        return allergyMapper.toDTO(getAllergyOrThrow(allergyPublicId, beneficiaryPublicId));
    }

    private Beneficiary getBeneficiaryOrThrow(UUID publicId) {
        return beneficiaryRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BeneficiaryNotFoundByPublicIdException(publicId));
    }

    private Allergy getAllergyOrThrow(UUID allergyPublicId, UUID beneficiaryPublicId) {
        Allergy allergy = allergyRepository.findByPublicId(allergyPublicId)
                .orElseThrow(() -> new AllergyNotFoundException(allergyPublicId));

        if (!allergy.belongsTo(beneficiaryPublicId)) {
            throw new AllergyNotOwnedByBeneficiaryException(allergyPublicId, beneficiaryPublicId);
        }

        return allergy;
    }
}
