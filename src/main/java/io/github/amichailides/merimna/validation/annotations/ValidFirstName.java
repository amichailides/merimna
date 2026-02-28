package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.validators.FirstNameValidator;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Size(min=2, max=20, message = "{name.size}", groups = FirstOrder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FirstNameValidator.class)
public @interface ValidFirstName {

    String message() default "{name.invalid}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
