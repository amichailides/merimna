package io.github.amichailides.merimna.beneficiary;


import io.github.amichailides.merimna.beneficiary.dto.*;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByPublicIdException;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.HouseUnitValidator;
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
    public BeneficiaryDetailsDTO findByPublicId(String publicId) {
        Beneficiary beneficiary = beneficiaryRepository.findWithDetailsByPublicId(publicId)
                .orElseThrow(() -> new BeneficiaryNotFoundByPublicIdException(publicId));

        return beneficiaryMapper.toDetailsDTO(beneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO create(BeneficiaryCreateDTO dto) {
        beneficiaryValidator.validateForSave(dto);

        HouseUnit houseUnit = houseUnitRepository.findByCode(dto.houseUnitCode())
                .orElseThrow(() -> new HouseUnitNotFoundByCodeException(dto.houseUnitCode()));

        houseUnitValidator.validateAssignmentForBeneficiary(houseUnit);

        Beneficiary beneficiary = beneficiaryMapper.toEntity(dto, houseUnit);
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
        return beneficiaryMapper.toDetailsDTO(savedBeneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO discharge(String publicId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(publicId);

        beneficiaryValidator.validateForDischarge(beneficiary); // business rules
        beneficiary.discharge(); // state check

        return beneficiaryMapper.toDetailsDTO(beneficiaryRepository.save(beneficiary));
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO updateBeneficiary(String publicId, BeneficiaryUpdateDTO dto) {
        // TODO(ADR-001): Support explicit null semantics in PATCH using JsonNullable
        // Currently null = no update

        Beneficiary beneficiary = getBeneficiaryOrThrow(publicId);

        beneficiaryValidator.validateForUpdate(beneficiary, dto);

        if (dto.houseUnit() != null) {
            HouseUnit targetHouseUnit = houseUnitRepository.findByCode(dto.houseUnit())
                    .orElseThrow(() -> new HouseUnitNotFoundByCodeException(dto.houseUnit()));

            if (!beneficiary.isAssignedTo(targetHouseUnit)) {
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
    public BeneficiaryListDTO changeHouseUnit(String publicId, String code) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(publicId);

        HouseUnit targetHouseUnit  = houseUnitRepository.findByCode(code)
                .orElseThrow(() -> new HouseUnitNotFoundByCodeException(code));

        if (!beneficiary.getHouseUnit().getCode().equals(targetHouseUnit.getCode())) {
            houseUnitValidator.validateAssignmentForBeneficiary(targetHouseUnit);
        }

        beneficiary.changeHouseUnit(targetHouseUnit );
        return beneficiaryMapper.toListDTO(beneficiary);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private Beneficiary getBeneficiaryOrThrow(String publicId) {
        return beneficiaryRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BeneficiaryNotFoundByPublicIdException(publicId));
    }

}