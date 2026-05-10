package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.validators.OptionalNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = OptionalNotBlankValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalNotBlank {

    String message() default "{value.notBlank}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}