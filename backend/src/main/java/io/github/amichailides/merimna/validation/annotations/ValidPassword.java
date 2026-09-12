package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.github.amichailides.merimna.validation.validators.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Size(min = 8, max = 128, message = "{user.password.size}", groups = SecondOrder.class)
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "{user.password.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
