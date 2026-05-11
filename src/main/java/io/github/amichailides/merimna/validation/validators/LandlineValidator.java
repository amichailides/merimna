package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidLandline;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LandlineValidator implements ConstraintValidator<ValidLandline, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String normalized = value.trim();

        if (normalized.isBlank()) {
            return false;
        }

        return normalized.matches(ValidationPatterns.PHONE);
    }
}
