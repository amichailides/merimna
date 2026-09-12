package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.AssignmentDateRange;
import io.github.amichailides.merimna.validation.annotations.ValidAssignmentDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AssignmentDateRangeValidator
        implements ConstraintValidator<ValidAssignmentDateRange, AssignmentDateRange> {

    @Override
    public boolean isValid(AssignmentDateRange dto, ConstraintValidatorContext context) {
        if (dto.startDate() == null || dto.endDate() == null) return true;
        return !dto.endDate().isBefore(dto.startDate());
    }
}