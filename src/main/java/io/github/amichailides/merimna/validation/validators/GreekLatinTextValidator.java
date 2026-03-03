package io.github.amichailides.merimna.validation.validators;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GreekLatinTextValidator implements ConstraintValidator<ValidGreekLatinText, String> {
    private int min;
    private int max;
    private boolean extended;

    @Override
    public void initialize(ValidGreekLatinText constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.extended = constraintAnnotation.extended();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        int length = value.length();
        if (length < min || length > max) {
            return false;
        }

        String pattern = extended
                ? ValidationPatterns.GREEK_LATIN_EXTENDED
                : ValidationPatterns.GREEK_LATIN_TEXT;
        return value.matches(pattern);
    }

}
