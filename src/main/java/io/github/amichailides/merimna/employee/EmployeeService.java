package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeDetailsDTO;

public interface EmployeeService {
    EmployeeDetailsDTO createEmployee(EmployeeCreateDTO dto);
    EmployeeDetailsDTO terminate(Long employeeId);
}
