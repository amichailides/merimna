package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.validation.annotations.ValidAssignmentDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AssignmentDateRangeValidator
        implements ConstraintValidator<ValidAssignmentDateRange, EmployeeAssignmentCreateDTO> {

    @Override
    public boolean isValid(EmployeeAssignmentCreateDTO dto, ConstraintValidatorContext context) {
        if (dto.startDate() == null || dto.endDate() == null) return true; // άλλα validators το πιάνουν
        return !dto.endDate().isBefore(dto.startDate());
    }
}
