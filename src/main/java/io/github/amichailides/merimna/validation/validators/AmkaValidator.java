package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AmkaValidator implements ConstraintValidator<ValidAmka, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Αν είναι null, επιστρέφουμε true (γιατί το null το ελέγχει το @NotNull)
        if (value == null || value.isBlank()) {
            return true;
        }

        return value.matches(ValidationPatterns.AMKA);
    }
}
