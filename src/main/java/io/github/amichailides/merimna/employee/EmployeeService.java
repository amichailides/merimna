package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeDetailsDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeListDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeDetailsDTO createEmployee(EmployeeCreateDTO dto);

    EmployeeDetailsDTO terminate(Long employeeId);

    Page<EmployeeListDTO> getAllEmployees(
            EmployeeSearchDTO criteria,
            Pageable pageable);

    EmployeeDetailsDTO getEmployeeById(Long id);
}
