package io.github.amichailides.merimna.employee;


import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeUpdateDTO;
import io.github.amichailides.merimna.employee.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EmployeeValidator {

    private final EmployeeRepository employeeRepository;

    public void validateForCreate(EmployeeCreateDTO dto) {
        if (employeeRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new EmployeeEmailAlreadyExistsException(dto.email());
        }
    }

    public void validateForUpdate(Employee existing, EmployeeUpdateDTO dto) {
        if (!existing.isActive()) {
            throw new EmployeeInactiveException();
        }

        boolean emailChanged = dto.email() != null && !dto.email().equalsIgnoreCase(existing.getEmail());

        if (emailChanged && employeeRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new EmployeeEmailAlreadyExistsException(dto.email());
        }
    }

    public void validateForTerminate(Employee employee, LocalDate terminationDate) {
        if (terminationDate.isBefore(employee.getHireDate())) {
            throw new EmployeeTerminationBeforeHireDateException(
                    employee.getHireDate(),
                    terminationDate
            );
        }
    }
}
