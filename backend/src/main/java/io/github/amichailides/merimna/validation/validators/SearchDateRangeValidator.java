package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.SearchDateRange;
import io.github.amichailides.merimna.validation.annotations.ValidSearchDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SearchDateRangeValidator
        implements ConstraintValidator<ValidSearchDateRange, SearchDateRange> {

    @Override
    public boolean isValid(SearchDateRange dto, ConstraintValidatorContext context) {
        if (dto.from() == null || dto.to() == null) return true;
        return !dto.to().isBefore(dto.from());
    }
}