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
        if (employeeRepository.existsByContactEmailIgnoreCase(dto.contactEmail())) {
            throw new EmployeeEmailAlreadyExistsException(dto.contactEmail());
        }
    }

    public void validateForUpdate(Employee existing, EmployeeUpdateDTO dto) {
        if (!existing.isActive()) {
            throw new EmployeeInactiveException();
        }

        boolean emailChanged = dto.contactEmail() != null && !dto.contactEmail().equalsIgnoreCase(existing.getContactEmail());

        if (emailChanged && employeeRepository.existsByContactEmailIgnoreCase(dto.contactEmail())) {
            throw new EmployeeEmailAlreadyExistsException(dto.contactEmail());
        }
    }

    public void validateForTerminate(Employee employee, LocalDate terminationDate) {
        if (terminationDate.isBefore(employee.getHireDate())) {
            throw new EmployeeTerminationBeforeHireDateException(
                    employee.getHireDate(),
                    terminationDate
            );
        }
        if (terminationDate.isAfter(LocalDate.now())) {
            throw new EmployeeTerminationDateInFutureException();
        }
    }
}
