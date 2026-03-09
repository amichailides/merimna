package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.validators.DateOfBirthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateOfBirthValidator.class)
public @interface ValidDateOfBirth {
    String message() default "{dob.invalid}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
