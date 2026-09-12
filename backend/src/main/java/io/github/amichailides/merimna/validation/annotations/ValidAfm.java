package io.github.amichailides.merimna.validation.annotations;


import io.github.amichailides.merimna.validation.validators.ValidAfmValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidAfmValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAfm {

    String message() default "{afm.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
