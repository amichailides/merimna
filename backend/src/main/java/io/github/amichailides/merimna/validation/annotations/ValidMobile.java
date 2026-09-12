package io.github.amichailides.merimna.validation.annotations;

import io.github.amichailides.merimna.validation.validators.MobileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileValidator.class)
public @interface ValidMobile {
    String message() default "{mobile.invalid}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}