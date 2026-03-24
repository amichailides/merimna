package io.github.amichailides.merimna.service;


import io.github.amichailides.merimna.dto.*;
import io.github.amichailides.merimna.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.mapper.BeneficiaryMapper;
import io.github.amichailides.merimna.domain.Beneficiary;
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

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAmka(String amka) {
        return beneficiaryRepository.existsByAmka(amka);
    }

    @Transactional(readOnly = true)
    public BeneficiaryReadOnlyDTO findById(Long id) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(id);
        return beneficiaryMapper.toReadOnlyDTO(beneficiary);
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
        Beneficiary beneficiary = getBeneficiaryOrThrow(id);

        beneficiary.discharge(); // state check
        validator.validateForDischarge(beneficiary); // business rules

        return beneficiaryMapper.toReadOnlyDTO(beneficiaryRepository.save(beneficiary));
    }

    @Transactional
    public BeneficiaryReadOnlyDTO updateBeneficiary(Long id, BeneficiaryUpdateDTO dto) {
        Beneficiary existing = getBeneficiaryOrThrow(id);

        validator.validateForUpdate(existing, dto);

        beneficiaryMapper.updateEntity(existing, dto);

        return beneficiaryMapper.toReadOnlyDTO(beneficiaryRepository.save(existing));
    }

    /**
     * <p>Όλα τα κριτήρια συνδυάζονται με AND.
     * Εξαίρεση: αν υπάρχει {@code amka}, το {@code q} αγνοείται.</p>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryReadOnlyDTO> findBeneficiaries(
            BeneficiarySearchDTO criteria,
            Pageable pageable) {

        Specification<Beneficiary> spec = (root, query, cb) -> cb.conjunction();

        // Αν υπάρχει amka, αγνοούμε το q
        if (hasText(criteria.getAmka())) {
            spec = spec.and(BeneficiarySpecifications.hasAmka(criteria.getAmka()));
        } else if (hasText(criteria.getQ())) {
            spec = spec.and(BeneficiarySpecifications.globalSearch(criteria.getQ()));
        }

        if (criteria.getHouseUnit() != null) {
            spec = spec.and(BeneficiarySpecifications.hasHouseUnit(criteria.getHouseUnit()));
        }

        if (!criteria.isIncludeInactive()) {
            spec = spec.and(BeneficiarySpecifications.isActive());
        }

        return beneficiaryRepository.findAll(spec, pageable)
                .map(beneficiaryMapper::toReadOnlyDTO);

    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private Beneficiary getBeneficiaryOrThrow(Long beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));
    }

}