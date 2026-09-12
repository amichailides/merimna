package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.validators.SearchDateRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SearchDateRangeValidator.class)
public @interface ValidSearchDateRange {
    String message() default "{search.date.range.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}