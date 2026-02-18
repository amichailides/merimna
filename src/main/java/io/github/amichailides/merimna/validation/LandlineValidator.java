package io.github.amichailides.merimna.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LandlineValidator implements ConstraintValidator<ValidLandline, String> {
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        return value.matches(ValidationPatterns.PHONE) || value.matches(ValidationPatterns.MOBILE);
    }
}
