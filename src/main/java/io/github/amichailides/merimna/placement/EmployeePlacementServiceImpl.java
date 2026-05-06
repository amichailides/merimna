package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.audit.event.EmployeePlacementCreatedEvent;
import io.github.amichailides.merimna.audit.event.EmployeePlacementTerminatedEvent;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeePlacement;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByPublicIdException;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundException;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementCreateDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementSearchDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementTerminateDTO;
import io.github.amichailides.merimna.placement.exception.EmployeePlacementNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static io.github.amichailides.merimna.placement.EmployeePlacementSpecifications.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeePlacementServiceImpl implements EmployeePlacementService{

    private final EmployeeRepository employeeRepository;
    private final HouseUnitRepository houseUnitRepository;
    private final EmployeePlacementRepository placementRepository;
    private final EmployeePlacementMapper placementMapper;
    private final EmployeePlacementValidator placementValidator;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public EmployeePlacementReadOnlyDTO create(EmployeePlacementCreateDTO dto) {
        Employee employee = employeeRepository.findByPublicId(dto.employeePublicId())
                .orElseThrow(() -> new EmployeeNotFoundByPublicIdException(dto.employeePublicId()));

        HouseUnit houseUnit = houseUnitRepository.findByPublicId(dto.houseUnitPublicId())
                .orElseThrow(() -> new HouseUnitNotFoundException(dto.houseUnitPublicId()));

        placementValidator.validateForCreate(employee, dto);

        EmployeePlacement placement = EmployeePlacement.create(
                employee,
                houseUnit,
                dto.startDate(),
                dto.endDate(),
                dto.reason()
        );

        employee.addPlacement(placement);
        EmployeePlacement saved = placementRepository.save(placement);

        eventPublisher.publishEvent(
                EmployeePlacementCreatedEvent.from(saved)
        );

        return placementMapper.toReadOnlyDTO(saved);
    }




    @Transactional(readOnly = true)
    public EmployeePlacementReadOnlyDTO getByPublicId(UUID publicId) {
        EmployeePlacement placement = placementRepository.findByPublicId(publicId)
                .orElseThrow(EmployeePlacementNotFoundException::new);

        return placementMapper.toReadOnlyDTO(placement);
    }

    @Transactional
    public void terminate(UUID publicId, EmployeePlacementTerminateDTO dto){
        EmployeePlacement placement = placementRepository.findByPublicId(publicId)
                .orElseThrow(EmployeePlacementNotFoundException::new);

        placement.close(dto.endDate());

        eventPublisher.publishEvent(
                EmployeePlacementTerminatedEvent.from(placement)
        );
    }

    @Transactional(readOnly = true)
    public Page<EmployeePlacementReadOnlyDTO> getAllPlacements(
            EmployeePlacementSearchDTO criteria,
            Pageable pageable) {

        boolean includeInactive = Boolean.TRUE.equals(criteria.includeInactive());

        Specification<EmployeePlacement> spec = Specification
                .where(hasEmployeePublicId(criteria.employeePublicId()))
                .and(hasHouseUnitPublicId(criteria.houseUnitPublicId()))
                .and(startDateFrom(criteria.startDateRange() != null ? criteria.startDateRange().from() : null))
                .and(startDateTo(criteria.startDateRange() != null ? criteria.startDateRange().to() : null))
                .and(endDateFrom(criteria.endDateRange() != null ? criteria.endDateRange().from() : null))
                .and(endDateTo(criteria.endDateRange() != null ? criteria.endDateRange().to() : null));

        if (!includeInactive) {
            spec = spec.and(isActive(LocalDate.now()));
        }

        return placementRepository.findAll(spec, pageable)
                .map(placementMapper::toReadOnlyDTO);
    }

}
