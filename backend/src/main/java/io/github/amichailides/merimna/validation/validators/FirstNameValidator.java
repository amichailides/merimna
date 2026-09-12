package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidFirstName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// TODO(#2): Improve validation error message granularity
public class FirstNameValidator implements ConstraintValidator<ValidFirstName, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.isBlank()) {
            return false;
        }
        return value.matches(ValidationPatterns.GREEK_LATIN_TEXT);
    }
}
