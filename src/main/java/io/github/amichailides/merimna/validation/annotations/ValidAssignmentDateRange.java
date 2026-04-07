package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.validators.AssignmentDateRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AssignmentDateRangeValidator.class)
public @interface ValidAssignmentDateRange {
    String message() default "{assignment.dateRange.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}