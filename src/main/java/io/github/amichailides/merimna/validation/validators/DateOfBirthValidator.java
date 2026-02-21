package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.annotations.ValidDateOfBirth;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;


public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, LocalDate> {
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate minAllowedDate = LocalDate.now().minusYears(100);
        return value.isAfter(minAllowedDate);
    }
}
