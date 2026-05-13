package io.github.amichailides.merimna.beneficiary;


import io.github.amichailides.merimna.access.HouseUnitAccessService;
import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.audit.event.BeneficiaryCreatedEvent;
import io.github.amichailides.merimna.audit.event.BeneficiaryDischargedEvent;
import io.github.amichailides.merimna.audit.event.BeneficiaryHouseUnitChangedEvent;
import io.github.amichailides.merimna.audit.event.BeneficiaryUpdatedEvent;
import io.github.amichailides.merimna.beneficiary.audit.BeneficiaryChangeDetector;
import io.github.amichailides.merimna.beneficiary.dto.*;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryAlreadyInHouseUnitException;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryNotFoundByPublicIdException;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.HouseUnitValidator;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundException;
import io.github.amichailides.merimna.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final HouseUnitAccessService houseUnitAccessService;
    private final ApplicationEventPublisher eventPublisher;
    private final CurrentUserProvider currentUserProvider;
    private final BeneficiaryChangeDetector beneficiaryChangeDetector;

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryDetailsDTO findByPublicId(UUID publicId) {
        Beneficiary beneficiary = beneficiaryRepository.findWithDetailsByPublicId(publicId)
                .orElseThrow(() -> new BeneficiaryNotFoundByPublicIdException(publicId));

        houseUnitAccessService.ensureCanAccess(beneficiary);

        return beneficiaryMapper.toDetailsDTO(beneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO create(BeneficiaryCreateDTO dto) {
        beneficiaryValidator.validateForSave(dto);

        HouseUnit houseUnit = houseUnitRepository.findByPublicId(dto.houseUnitPublicId())
                .orElseThrow(() -> new HouseUnitNotFoundException(dto.houseUnitPublicId()));

        houseUnitAccessService.ensureCanAccess(houseUnit);
        houseUnitValidator.ensureCanAcceptBeneficiary(houseUnit);

        Beneficiary beneficiary = beneficiaryMapper.toEntity(dto, houseUnit);
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);

        eventPublisher.publishEvent(
                BeneficiaryCreatedEvent.from(savedBeneficiary)
        );

        return beneficiaryMapper.toDetailsDTO(savedBeneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO discharge(UUID publicId, DischargeRequestDTO dto) {
        Beneficiary beneficiary = getBeneficiaryOrThrow(publicId);
        houseUnitAccessService.ensureCanAccess(beneficiary);

        beneficiaryValidator.validateForDischarge(beneficiary);
        Employee dischargedBy = currentUserProvider.getCurrentEmployee();

        beneficiary.discharge(
                dto.dischargeDate(),
                dto.dischargeReason(),
                dischargedBy
        );

        eventPublisher.publishEvent(
                BeneficiaryDischargedEvent.from(beneficiary)
        );

        return beneficiaryMapper.toDetailsDTO(beneficiary);
    }

    @Override
    @Transactional
    public BeneficiaryDetailsDTO updateBeneficiary(UUID publicId, BeneficiaryUpdateDTO dto) {
        // TODO(ADR-001): Support explicit null semantics in PATCH using JsonNullable
        // Currently null = no update

        Beneficiary beneficiary = getBeneficiaryOrThrow(publicId);

        houseUnitAccessService.ensureCanAccess(beneficiary);
        beneficiaryValidator.validateForUpdate(beneficiary, dto);

        EntityChangeSet changeSet = beneficiaryChangeDetector.detectChanges(beneficiary, dto);

        if (!changeSet.hasChanges()) {
            return beneficiaryMapper.toDetailsDTO(beneficiary);
        }

        beneficiaryMapper.updateEntity(beneficiary, dto);

        eventPublisher.publishEvent(
                BeneficiaryUpdatedEvent.from(beneficiary, changeSet));

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
                houseUnitAccessService.resolveHouseUnitScope();

        if (houseUnitScope.isPresent()) {
            Set<HouseUnit> accessibleHouseUnits = houseUnitScope.get();

            spec = spec.and(BeneficiarySpecifications.inHouseUnits(accessibleHouseUnits));

            if (criteria.getHouseUnitPublicId() != null) {
                boolean hasAccess = accessibleHouseUnits.stream()
                        .anyMatch(h -> criteria.getHouseUnitPublicId().equals(h.getPublicId()));

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

        if (criteria.getHouseUnitPublicId() != null) {
            spec = spec.and(BeneficiarySpecifications.hasHouseUnitPublicId(
                    criteria.getHouseUnitPublicId()));
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
        houseUnitAccessService.ensureCanAccess(beneficiary);

        HouseUnit targetHouseUnit = houseUnitRepository.findByPublicId(houseUnitPublicId)
                .orElseThrow(() -> new HouseUnitNotFoundException(houseUnitPublicId));

        // TODO(#26): Revisit beneficiary house-unit transfer policy.
        // Current V1 policy requires access to both source and target house units
        houseUnitAccessService.ensureCanAccess(targetHouseUnit);
        HouseUnit sourceHouseUnit = beneficiary.getHouseUnit();

        if (beneficiary.isAssignedTo(targetHouseUnit)) {
            throw new BeneficiaryAlreadyInHouseUnitException(
                    beneficiary.getPublicId(),
                    targetHouseUnit.getPublicId()
            );
        }

        houseUnitValidator.ensureCanAcceptBeneficiary(targetHouseUnit);
        beneficiary.changeHouseUnit(targetHouseUnit);

        eventPublisher.publishEvent(BeneficiaryHouseUnitChangedEvent.from(
                beneficiary,
                sourceHouseUnit,
                targetHouseUnit)
        );

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