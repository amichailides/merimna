package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidMobile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class MobileValidator implements ConstraintValidator<ValidMobile, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Αν είναι άδειο, το αφήνουμε να περάσει (το ελέγχει ο class-level validator αν λείπουν και τα δύο)
        if (value == null || value.isBlank()) {
            return true;
        }

        return Pattern.matches(ValidationPatterns.MOBILE, value);
    }
}