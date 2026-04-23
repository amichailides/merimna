package io.github.amichailides.merimna.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.hibernate.validator.constraints.UUID;

import java.lang.annotation.*;

@UUID
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {
    String message() default "{publicId.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
