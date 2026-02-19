package io.github.amichailides.merimna.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotBlank(message = "{amka.required}", groups = FirstOrder.class)
@Size(min = 11, max = 11, message = "{amka.size}", groups = FirstOrder.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AmkaValidator.class)
public @interface ValidAmka {
    String message() default "{amka.invalid}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
