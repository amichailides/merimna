package io.github.amichailides.merimna.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FirstNameValidator implements ConstraintValidator<ValidFirstName, String> {
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return value.matches(ValidationPatterns.GREEK_LATIN_TEXT);
    }
}
