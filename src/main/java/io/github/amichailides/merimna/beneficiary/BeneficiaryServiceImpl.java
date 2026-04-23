package io.github.amichailides.merimna.beneficiary;


import io.github.amichailides.merimna.beneficiary.dto.*;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByPublicIdException;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.HouseUnitValidator;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


/**
 * Υλοποίηση με business validation μέσω του {@link BeneficiaryValidator}
 * και βελτιστοποιημένα {@code readOnly = true} transactions.
 */
@Service
@RequiredArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final BeneficiaryMapper beneficiaryMapper;
    private final BeneficiaryValidator beneficiaryValidator;
    private final HouseUnitRepository houseUnitRepository;
    private final HouseUnitValidator houseUnitValidator;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAmka(String amka) {
        return beneficiaryRepository.existsByAmka(amka);
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryDetailsDTO findByPublicId(UUID publicId) {
        Beneficiary beneficiary = beneficiaryRepository.findWithDetailsByPublicId(publicId)
                .orElseThrow(() -> new BeneficiaryNotFoundByPublicIdException(publicId));

        return beneficiaryMapper.toDetailsDTO(beneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO create(BeneficiaryCreateDTO dto) {
        beneficiaryValidator.validateForSave(dto);

        HouseUnit houseUnit = houseUnitRepository.findByPublicId(dto.houseUnitPublicId())
                .orElseThrow(() -> new HouseUnitNotFoundException(dto.houseUnitPublicId()));

        houseUnitValidator.validateAssignmentForBeneficiary(houseUnit);

        Beneficiary beneficiary = beneficiaryMapper.toEntity(dto, houseUnit);
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
        return beneficiaryMapper.toDetailsDTO(savedBeneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO discharge(UUID publicId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(publicId);

        beneficiaryValidator.validateForDischarge(beneficiary); // business rules
        beneficiary.discharge(); // state check

        return beneficiaryMapper.toDetailsDTO(beneficiaryRepository.save(beneficiary));
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO updateBeneficiary(UUID publicId, BeneficiaryUpdateDTO dto) {
        // TODO(ADR-001): Support explicit null semantics in PATCH using JsonNullable
        // Currently null = no update

        Beneficiary beneficiary = getBeneficiaryOrThrow(publicId);

        beneficiaryValidator.validateForUpdate(beneficiary, dto);

        if (dto.houseUnitPublicId() != null) {
            HouseUnit targetHouseUnit = houseUnitRepository.findByPublicId(dto.houseUnitPublicId())
                    .orElseThrow(() -> new HouseUnitNotFoundException(dto.houseUnitPublicId()));

            if (beneficiary.isNotAssignedTo(targetHouseUnit)) {
                houseUnitValidator.validateAssignmentForBeneficiary(targetHouseUnit);
                beneficiary.changeHouseUnit(targetHouseUnit);
            }
        }

        beneficiaryMapper.updateEntity(beneficiary, dto);

        return beneficiaryMapper.toDetailsDTO(beneficiary);
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
    public BeneficiaryListDTO changeHouseUnit(UUID beneficiaryPublicId, UUID houseUnitPublicId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(beneficiaryPublicId);

        HouseUnit targetHouseUnit  = houseUnitRepository.findByPublicId(houseUnitPublicId)
                .orElseThrow(() -> new HouseUnitNotFoundException(houseUnitPublicId));

        if (beneficiary.isNotAssignedTo(targetHouseUnit)) {
            houseUnitValidator.validateAssignmentForBeneficiary(targetHouseUnit);
            beneficiary.changeHouseUnit(targetHouseUnit);
        }

        beneficiary.changeHouseUnit(targetHouseUnit );
        return beneficiaryMapper.toListDTO(beneficiary);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private Beneficiary getBeneficiaryOrThrow(UUID publicId) {
        return beneficiaryRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BeneficiaryNotFoundByPublicIdException(publicId));
    }

}