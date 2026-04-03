package io.github.amichailides.merimna.beneficiary;


import io.github.amichailides.merimna.beneficiary.dto.*;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByIdException;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundByCodeException;
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
    private final HouseUnitRepository houseUnitRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAmka(String amka) {
        return beneficiaryRepository.existsByAmka(amka);
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryDetailsDTO findById(Long id) {
        Beneficiary beneficiary = beneficiaryRepository.findWithDetailsById(id)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(id));

        return beneficiaryMapper.toDetailsDTO(beneficiary);
    }

    @Transactional
    public BeneficiaryDetailsDTO save(BeneficiarySaveDTO dto) {

        validator.validateForSave(dto);

        HouseUnit houseUnit = houseUnitRepository.findByCode(dto.houseUnitCode())
                .orElseThrow(() -> new HouseUnitNotFoundByCodeException(dto.houseUnitCode()));

        // TODO(#12): Add domain validation for HouseUnit assignment during beneficiary creation

        Beneficiary beneficiary = beneficiaryMapper.toEntity(dto, houseUnit);
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
        return beneficiaryMapper.toDetailsDTO(savedBeneficiary);
    }

    @Transactional
    public BeneficiaryDetailsDTO discharge(Long id) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(id);

        validator.validateForDischarge(beneficiary); // business rules
        beneficiary.discharge(); // state check

        return beneficiaryMapper.toDetailsDTO(beneficiaryRepository.save(beneficiary));
    }

    @Transactional
    public BeneficiaryDetailsDTO updateBeneficiary(Long id, BeneficiaryUpdateDTO dto) {
        // TODO(ADR-001): Support explicit null semantics in PATCH using JsonNullable
        // Currently null = no update

        Beneficiary employee = getBeneficiaryOrThrow(id);

        validator.validateForUpdate(employee, dto);

        beneficiaryMapper.updateEntity(employee, dto);

        return beneficiaryMapper.toDetailsDTO(employee);
    }

    /**
     * <p>Όλα τα κριτήρια συνδυάζονται με AND.
     * Εξαίρεση: αν υπάρχει {@code amka}, το {@code q} αγνοείται.</p>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BeneficiaryListDTO> findBeneficiaries(
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
                .map(beneficiaryMapper::toListDTO);
    }

    @Override
    @Transactional
    // TODO(#10): Add domain validation for houseUnit assignment
    public BeneficiaryListDTO changeHouseUnit(Long beneficiaryId, String code) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryId);

        HouseUnit houseUnit = houseUnitRepository.findByCode(code)
                .orElseThrow(() -> new HouseUnitNotFoundByCodeException(code));

        beneficiary.changeHouseUnit(houseUnit);
        return beneficiaryMapper.toListDTO(beneficiary);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private Beneficiary getBeneficiaryOrThrow(Long beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BeneficiaryNotFoundByIdException(beneficiaryId));
    }

}