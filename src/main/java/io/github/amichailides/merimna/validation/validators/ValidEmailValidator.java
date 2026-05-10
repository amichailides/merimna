package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value.isBlank()) {
            return false;
        }

        return value.matches(ValidationPatterns.EMAIL);
    }
}
