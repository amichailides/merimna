package io.github.amichailides.merimna.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotBlank(message = "{amka.required}")
@Size(min = 3, max = 20, message = "{name.size}")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AmkaValidator.class)
public @interface ValidAmka {
    String message() default "{name.invalid}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
