package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.employee.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface EmployeeService {
    EmployeeDetailsDTO createEmployee(EmployeeCreateDTO dto);

    EmployeeDetailsDTO terminate(Long employeeId, LocalDate dto);

    Page<EmployeeListDTO> getAllEmployees(
            EmployeeSearchDTO criteria,
            Pageable pageable);

    EmployeeDetailsDTO getEmployeeById(Long id);

    EmployeeDetailsDTO updateEmployee(Long id, EmployeeUpdateDTO dto);

    EmployeeDetailsDTO reactivate(Long employeeId);
}
