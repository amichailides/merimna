package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.assignment.AssignmentType;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.dto.*;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByIdException;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundByCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final HouseUnitRepository houseUnitRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;

    @Override
    @Transactional
    public EmployeeDetailsDTO createEmployee(EmployeeCreateDTO dto) {
        //TODO validator εδω...

        Employee employee = employeeMapper.toEntity(dto);

        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toDetailsDTO(saved);
    }


    @Override
    @Transactional
    public EmployeeDetailsDTO terminate(Long employeeId) {
        Employee employee = getEmployeeOrThrow(employeeId);

        // TODO(#12): Add business validation for termination
        employee.terminate();

        return employeeMapper.toDetailsDTO(employee);
    }

    @Override
    @Transactional
    public EmployeeDetailsDTO reactivate(Long employeeId) {
        Employee employee = getEmployeeOrThrow(employeeId);

        // TODO(#12): Add business validation for reactivation
        employee.reactivate();

        return employeeMapper.toDetailsDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeListDTO> getAllEmployees(
            EmployeeSearchDTO criteria,
            Pageable pageable) {

        boolean includeInactive = Boolean.TRUE.equals(criteria.includeInactive());

        Specification<Employee> spec = Specification.where(
                        EmployeeSpecifications.globalSearch(criteria.q()))
                .and(EmployeeSpecifications.hasPosition(criteria.position()))
                .and(EmployeeSpecifications.isActive(includeInactive ? null : true));

        if (criteria.houseUnit() != null && !criteria.houseUnit().isBlank()) {
            spec = spec.and(EmployeeSpecifications.belongsToHouseUnit(criteria.houseUnit()));
        }

        return employeeRepository.findAll(spec, pageable)
                .map(employeeMapper::toListDTO);
    }

    @Override
    @Transactional
    public EmployeeDetailsDTO updateEmployee(Long id, EmployeeUpdateDTO dto) {
        // TODO(ADR-001): Support explicit null semantics in PATCH using JsonNullable
        // Currently null = no update

        Employee employee = getEmployeeOrThrow(id);
        employeeValidator.validateForUpdate(employee, dto);

        employeeMapper.updateEntity(employee, dto);

        return employeeMapper.toDetailsDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDetailsDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findWithDetailsById(id)
                .orElseThrow(() -> new EmployeeNotFoundByIdException(id));

        return employeeMapper.toDetailsDTO(employee);
    }

    @Transactional
    public void assignEmployeeToHouseUnit(
            Long employeeId,
            String houseUnitCode,
            AssignmentType assignmentType,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundByIdException(employeeId));
        HouseUnit houseUnit = houseUnitRepository.findByCode(houseUnitCode)
                .orElseThrow(() -> new HouseUnitNotFoundByCodeException(houseUnitCode));

        employee.assignToHouseUnit(houseUnit, assignmentType, startDate, endDate);
        employeeRepository.save(employee);
    }

    private Employee getEmployeeOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundByIdException(employeeId));
    }
}
