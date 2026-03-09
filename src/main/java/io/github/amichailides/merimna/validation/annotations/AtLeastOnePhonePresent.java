package io.github.amichailides.merimna.validation.annotations;



import io.github.amichailides.merimna.validation.validators.AtLeastOnePhonePresentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // Πάνω στην κλάση
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOnePhonePresentValidator.class)
public @interface AtLeastOnePhonePresent {
    String message() default "{contact.missing}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}