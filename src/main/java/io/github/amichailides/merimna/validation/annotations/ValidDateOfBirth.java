package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.validators.DateOfBirthValidator;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateOfBirthValidator.class)
public @interface ValidDateOfBirth {
    String message() default "{name.invalid}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
