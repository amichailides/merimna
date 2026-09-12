package io.github.amichailides.merimna.employee.onboarding.dto;

import io.github.amichailides.merimna.validation.annotations.ValidEmail;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.NotBlank;

public record SystemAccessRequest(

        @NotBlank(
                message = "{employee.onboarding.systemAccess.accountEmail.required}",
                groups = FirstOrder.class
        )
        @ValidEmail(
                message = "{employee.onboarding.systemAccess.accountEmail.invalid}",
                groups = SecondOrder.class
        )
        String accountEmail

) {}