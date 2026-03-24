package io.github.amichailides.merimna.service;

import io.github.amichailides.merimna.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.exception.AllergyNotFoundException;
import io.github.amichailides.merimna.exception.AllergyNotOwnedByBeneficiaryException;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.mapper.AllergyMapper;
import io.github.amichailides.merimna.domain.Allergy;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.repository.AllergyRepository;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
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
    public AllergyReadOnlyDTO addAllergy(Long beneficiaryId, AllergyCreateDTO dto) {

        Beneficiary existing = getBeneficiaryOrThrow(beneficiaryId);

        Allergy allergy = allergyMapper.toEntity(dto);
        existing.addAllergy(allergy);
        Allergy savedAllergy = allergyRepository.save(allergy);

        return allergyMapper.toDTO(savedAllergy);
    }

    @Override
    @Transactional
    public AllergyReadOnlyDTO updateAllergy(Long beneficiaryId, Long allergyId, AllergyUpdateDTO dto) {

        Allergy allergy = getAllergyOrThrow(allergyId, beneficiaryId);

        allergyMapper.updateEntity(dto, allergy);
        // dirty checking  αυτόματο UPDATE στο commit
        return allergyMapper.toDTO(allergy);
    }

    @Override
    @Transactional
    public void deleteAllergy(Long beneficiaryId, Long allergyId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);

        // TODO: AllergyValidator. business rules πχ duplicate allergy check,
        //  σοβαρότητα αλλεργίας σε συνδυασμό με ενεργά φάρμακα (cross-check με Medication)
        Allergy allergy = getAllergyOrThrow(allergyId, beneficiaryId);

        // orphanRemoval=true  Hibernate τη σβήνει αυτόματα
        beneficiary.removeAllergy(allergy);
    }

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

    @Transactional(readOnly = true)
    public AllergyReadOnlyDTO getAllergy(Long beneficiaryId, Long allergyId) {
        return allergyMapper.toDTO(getAllergyOrThrow(allergyId, beneficiaryId));
    }

    private Beneficiary getBeneficiaryOrThrow (Long beneficiaryId) {

        return  beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));
    }

    private Allergy getAllergyOrThrow (Long allergyId, Long beneficiaryId) {
        return  allergyRepository.findByIdAndBeneficiaryId(allergyId, beneficiaryId)
                .orElseThrow(() -> {
                    if (!allergyRepository.existsById(allergyId)) {
                        return new AllergyNotFoundException(allergyId);
                    }
                    return new AllergyNotOwnedByBeneficiaryException(allergyId, beneficiaryId);
                });
    }
}
