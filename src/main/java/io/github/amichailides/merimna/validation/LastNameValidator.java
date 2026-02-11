package io.github.amichailides.merimna.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LastNameValidator implements ConstraintValidator<ValidLastName, String> {
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        return value.matches("^[A-Za-zΑ-Ωα-ωΆ-ώ\\s-]+$");
    }
}
