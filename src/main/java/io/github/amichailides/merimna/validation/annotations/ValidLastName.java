package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.validators.LastNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotBlank(message = "{lastname.required}", groups = FirstOrder.class)
@Size( min = 2, max = 20, message = "{lastname.size}", groups = FirstOrder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LastNameValidator.class)
public @interface ValidLastName {
    String message() default "{lastname.invalid}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
