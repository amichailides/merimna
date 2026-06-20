package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.employee.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface EmployeeService {
    EmployeeDetailsDTO createEmployee(EmployeeCreateDTO dto);

    EmployeeDetailsDTO terminate(UUID publicId, LocalDate terminationDate);

    Page<EmployeeListDTO> getAllEmployees(
            EmployeeSearchDTO criteria,
            Pageable pageable);

    EmployeeDetailsDTO getEmployeeByPublicId(UUID publicId);

    Page<EmployeeActivityDTO> getEmployeeActivity(UUID publicId, Pageable pageable);

    EmployeeDetailsDTO updateEmployee(UUID publicId, EmployeeUpdateDTO dto);

    EmployeeDetailsDTO reactivate(UUID publicId);
}
