package io.github.amichailides.merimna.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AmkaValidator implements ConstraintValidator<ValidAmka, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Αν είναι null, επιστρέφουμε true (γιατί το null το ελέγχει το @NotNull)
        if (value == null || value.isBlank()) {
            return true;
        }

        return value.matches("\\d{11}");
    }
}
