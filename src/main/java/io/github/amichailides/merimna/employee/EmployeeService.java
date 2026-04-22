package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.employee.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface EmployeeService {
    EmployeeDetailsDTO createEmployee(EmployeeCreateDTO dto);

    EmployeeDetailsDTO terminate(String publicId, LocalDate terminationDate);

    Page<EmployeeListDTO> getAllEmployees(
            EmployeeSearchDTO criteria,
            Pageable pageable);

    EmployeeDetailsDTO getEmployeeByPublicId(String publicId);

    EmployeeDetailsDTO updateEmployee(String publicId, EmployeeUpdateDTO dto);

    EmployeeDetailsDTO reactivate(String publicId);
}
