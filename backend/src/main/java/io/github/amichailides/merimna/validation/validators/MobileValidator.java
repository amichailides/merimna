package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidMobile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<ValidMobile, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String normalized = value.trim();

        if (normalized.isBlank()) {
            return false;
        }

        return normalized.matches(ValidationPatterns.MOBILE);
    }
}