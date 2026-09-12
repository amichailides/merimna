package io.github.amichailides.merimna.employee;


import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeUpdateDTO;
import io.github.amichailides.merimna.employee.exception.*;
import io.github.amichailides.merimna.exception.ConflictValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmployeeValidator {

    private final EmployeeRepository employeeRepository;

    public void validateForCreate(EmployeeCreateDTO dto) {
        if (employeeRepository.existsByContactEmailIgnoreCase(dto.contactEmail())) {
            throw emailConflict();
        }
    }

    public void validateForUpdate(Employee existing, EmployeeUpdateDTO dto) {
        if (!existing.isActive()) {
            throw new EmployeeInactiveException(existing.getPublicId());
        }

        boolean emailChanged = dto.contactEmail() != null && !dto.contactEmail().equalsIgnoreCase(existing.getContactEmail());

        if (emailChanged && employeeRepository.existsByContactEmailIgnoreCase(dto.contactEmail())) {
            throw emailConflict();
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
            throw new EmployeeTerminationDateInFutureException(
                    employee.getPublicId(),
                    terminationDate
            );
        }
    }


    private ConflictValidationException emailConflict() {
        Map<String, String> conflicts = new LinkedHashMap<>();
        conflicts.put("contactEmail", ErrorCode.EMPLOYEE_EMAIL_ALREADY_EXISTS.getMessageKey());
        return new ConflictValidationException(conflicts);
    }
}
