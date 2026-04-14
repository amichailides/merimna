package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.annotations.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) {
            return true;
        }

        if (password.chars().anyMatch(Character::isWhitespace)) {
            return false;
        }

        if (password.chars().distinct().count() == 1) {
            return false;
        }

        if (password.chars().allMatch(Character::isDigit)) {
            return false;
        }

        return true;
    }
}
