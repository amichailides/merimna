package io.github.amichailides.merimna.service;


import io.github.amichailides.merimna.dto.*;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByAmkaException;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.mapper.AllergyMapper;
import io.github.amichailides.merimna.mapper.BeneficiaryMapper;
import io.github.amichailides.merimna.model.Allergy;
import io.github.amichailides.merimna.model.Beneficiary;
import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.repository.AllergyRepository;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import io.github.amichailides.merimna.service.validation.BeneficiaryValidator;
import io.github.amichailides.merimna.specification.BeneficiarySpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Υλοποίηση με business validation μέσω του {@link BeneficiaryValidator}
 * και βελτιστοποιημένα {@code readOnly = true} transactions.
 */
@Service
@RequiredArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final BeneficiaryMapper beneficiaryMapper;
    private final BeneficiaryValidator validator;
    private final AllergyMapper allergyMapper;
    private final AllergyRepository allergyRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryReadOnlyDTO> findAllBeneficiaries(
            boolean includeInactive,
            HouseUnit houseUnit,
            Pageable pageable) {

        Page<Beneficiary> beneficiariesPage;

        if (houseUnit != null) {
            beneficiariesPage = includeInactive
                    ? beneficiaryRepository.findAllByHouseUnit(houseUnit, pageable)
                    : beneficiaryRepository.findAllByHouseUnitAndIsActiveTrue(houseUnit, pageable);
        } else {
            beneficiariesPage = includeInactive
                    ? beneficiaryRepository.findAll(pageable)
                    : beneficiaryRepository.findAllByIsActiveTrue(pageable);
        }


        return beneficiariesPage.map(beneficiaryMapper::toReadOnlyDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryReadOnlyDTO findByAmka(String amka) {
        return beneficiaryRepository.findByAmka(amka)
                .map(beneficiaryMapper::toReadOnlyDTO)
                .orElseThrow(() -> new BeneficiaryNotFoundByAmkaException(amka));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAmka(String amka) {
        return beneficiaryRepository.existsByAmka(amka);
    }

    @Transactional(readOnly = true)
    public BeneficiaryReadOnlyDTO findById(Long id) {
        return beneficiaryRepository.findById(id)
                .map(beneficiaryMapper::toReadOnlyDTO)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(id));
    }

    @Transactional
    public BeneficiaryReadOnlyDTO save(BeneficiarySaveDTO dto) {

        validator.validateForSave(dto);

        Beneficiary beneficiary = beneficiaryMapper.toEntity(dto);
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
        return beneficiaryMapper.toReadOnlyDTO(savedBeneficiary);
    }

    @Transactional
    public BeneficiaryReadOnlyDTO discharge(Long id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(id));

        beneficiary.discharge(); // state check
        validator.validateForDischarge(beneficiary); // business rules

        return beneficiaryMapper.toReadOnlyDTO(beneficiaryRepository.save(beneficiary));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryReadOnlyDTO> search(String term, Pageable pageable) {
        //  Παίρνουμε το specification που φτιάξαμε
        Specification<Beneficiary> spec = BeneficiarySpecifications.globalSearch(term);

        // 2. Ζητάμε από το repository να κάνει το search με pagination
        // 3. Κάνουμε map κάθε Entity που βρέθηκε σε ReadOnlyDTO
        return beneficiaryRepository.findAll(spec, pageable)
                .map(beneficiaryMapper::toReadOnlyDTO);
    }

    @Transactional
    public BeneficiaryReadOnlyDTO updateBeneficiary(Long id, BeneficiaryUpdateDTO dto) {
        Beneficiary existing = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(id));

        validator.validateForUpdate(existing,dto);

        beneficiaryMapper.updateEntity(dto, existing);

        return beneficiaryMapper.toReadOnlyDTO(beneficiaryRepository.save(existing));
    }

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
                //TODO μην ξεχάσεις custom exception
                .orElseThrow( () -> new RuntimeException(String.valueOf(allergyId)));

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
                //TODO μην ξεχάσεις custom exception
                .orElseThrow(() -> new RuntimeException(String.valueOf(allergyId)));

        // orphanRemoval=true → Hibernate τη σβήνει αυτόματα
        existing.removeAllergy(allergy);
    }



}