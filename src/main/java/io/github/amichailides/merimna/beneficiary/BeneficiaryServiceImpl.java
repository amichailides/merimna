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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
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
    private final BeneficiaryAccessService beneficiaryAccessService;

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

        beneficiaryAccessService.checkCanAccess(beneficiary);

        return beneficiaryMapper.toDetailsDTO(beneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO create(BeneficiaryCreateDTO dto) {
        beneficiaryValidator.validateForSave(dto);

        HouseUnit houseUnit = houseUnitRepository.findByPublicId(dto.houseUnitPublicId())
                .orElseThrow(() -> new HouseUnitNotFoundException(dto.houseUnitPublicId()));

        beneficiaryAccessService.checkCanAccess(houseUnit);
        houseUnitValidator.validateAssignmentForBeneficiary(houseUnit);

        Beneficiary beneficiary = beneficiaryMapper.toEntity(dto, houseUnit);
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
        return beneficiaryMapper.toDetailsDTO(savedBeneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO discharge(UUID publicId) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(publicId);
        beneficiaryAccessService.checkCanAccess(beneficiary);

        beneficiaryValidator.validateForDischarge(beneficiary);
        beneficiary.discharge();

        return beneficiaryMapper.toDetailsDTO(beneficiaryRepository.save(beneficiary));
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO updateBeneficiary(UUID publicId, BeneficiaryUpdateDTO dto) {
        // TODO(ADR-001): Support explicit null semantics in PATCH using JsonNullable
        // Currently null = no update

        Beneficiary beneficiary = getBeneficiaryOrThrow(publicId);

        beneficiaryAccessService.checkCanAccess(beneficiary);
        beneficiaryValidator.validateForUpdate(beneficiary, dto);

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

        Optional<Set<HouseUnit>> houseUnitScope =
                beneficiaryAccessService.resolveHouseUnitScope();

        if (houseUnitScope.isPresent()) {
            Set<HouseUnit> accessibleHouseUnits = houseUnitScope.get();

            spec = spec.and(BeneficiarySpecifications.inHouseUnits(accessibleHouseUnits));

            if (hasText(criteria.getHouseUnit())) {
                boolean hasAccess = accessibleHouseUnits.stream()
                        .anyMatch(h -> criteria.getHouseUnit().equals(h.getCode()));

                if (!hasAccess) {
                    throw new AccessDeniedException("No access to this house unit");
                }
            }
        }

        if (hasText(criteria.getAmka())) {
            spec = spec.and(BeneficiarySpecifications.hasAmka(criteria.getAmka()));
        } else if (hasText(criteria.getQ())) {
            spec = spec.and(BeneficiarySpecifications.globalSearch(criteria.getQ()));
        }

        if (hasText(criteria.getHouseUnit())) {
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
        beneficiaryAccessService.checkCanAccess(beneficiary);

        HouseUnit targetHouseUnit  = houseUnitRepository.findByPublicId(houseUnitPublicId)
                .orElseThrow(() -> new HouseUnitNotFoundException(houseUnitPublicId));

        // TODO(#26): Revisit beneficiary house-unit transfer policy.
        // Current V1 policy requires access to both source and target house units
        beneficiaryAccessService.checkCanAccess(targetHouseUnit);

        if (beneficiary.isNotAssignedTo(targetHouseUnit)) {
            houseUnitValidator.validateAssignmentForBeneficiary(targetHouseUnit);
            beneficiary.changeHouseUnit(targetHouseUnit);
        }

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