package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.validators.ValidEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidEmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

    String message() default "{email.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
