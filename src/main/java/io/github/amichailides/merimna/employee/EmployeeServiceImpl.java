package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeDetailsDTO;
import io.github.amichailides.merimna.employee.exception.EmployeeNotFoundByIdException;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundByCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final HouseUnitRepository houseUnitRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;

    @Transactional
    public EmployeeDetailsDTO createEmployee(EmployeeCreateDTO dto) {
        //TODO validator εδω...

        Set<HouseUnit> houseUnit = dto.houseUnitCodes().stream()
                .map(code -> houseUnitRepository.findByCode(code)
                .orElseThrow(() -> new HouseUnitNotFoundByCodeException(code)))
                .collect(Collectors.toSet());


        Employee employee = employeeMapper.toEntity(dto, houseUnit);
        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toDTO(saved);
    }


    @Transactional
    public EmployeeDetailsDTO terminate(Long employeeId) {
        Employee employee = getEmployeeOrThrow(employeeId);

        employeeValidator.validateForTermination(employee);
        employee.deactivate();

        return employeeMapper.toDTO(employee);
    }

    private Employee getEmployeeOrThrow(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundByIdException(employeeId));
    }


}
