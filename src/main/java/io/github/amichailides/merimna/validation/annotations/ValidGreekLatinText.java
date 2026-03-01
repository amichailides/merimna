package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.validators.GreekLatinTextValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GreekLatinTextValidator.class)
public @interface ValidGreekLatinText {
    int min() default 2;
    int max() default 100;
    String message() default "{text.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
