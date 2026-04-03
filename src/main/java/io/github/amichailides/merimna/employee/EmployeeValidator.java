package io.github.amichailides.merimna.employee;


import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.employee.dto.EmployeeUpdateDTO;
import io.github.amichailides.merimna.employee.exception.EmployeeEmailAlreadyExistsException;
import io.github.amichailides.merimna.employee.exception.EmployeeInactiveException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeValidator {

    private final EmployeeRepository repository;

    public void validateForUpdate(Employee existing, EmployeeUpdateDTO dto) {
        if (!existing.isActive()) {
            throw new EmployeeInactiveException();
        }

        boolean emailChanged = dto.email() != null && !dto.email().equalsIgnoreCase(existing.getEmail());

        if (emailChanged && repository.existsByEmailIgnoreCase(dto.email())) {
            throw new EmployeeEmailAlreadyExistsException(dto.email());
        }
    }
}
