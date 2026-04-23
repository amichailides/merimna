package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeePlacement;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByPublicIdException;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundException;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeePlacementServiceImpl implements EmployeePlacementService{

    private final EmployeeRepository employeeRepository;
    private final HouseUnitRepository houseUnitRepository;
    private final EmployeePlacementRepository placementRepository;
    private final EmployeePlacementMapper placementMapper;

    @Transactional
    public EmployeePlacementReadOnlyDTO create(EmployeePlacementCreateDTO dto) {
        Employee employee = employeeRepository.findByPublicId(dto.employeePublicId())
                .orElseThrow(() -> new EmployeeNotFoundByPublicIdException(dto.employeePublicId()));

        HouseUnit houseUnit = houseUnitRepository.findByPublicId(dto.houseUnitPublicId())
                .orElseThrow(() -> new HouseUnitNotFoundException(dto.houseUnitPublicId()));

        EmployeePlacement placement = EmployeePlacement.create(
                employee,
                houseUnit,
                dto.startDateTime(),
                dto.endDateTime(),
                dto.reason()
        );

        EmployeePlacement saved = placementRepository.save(placement);
        return placementMapper.toReadOnlyDTO(saved);
    }

}
