package io.github.amichailides.merimna.security.auth.dto;

import io.github.amichailides.merimna.validation.annotations.ValidEmail;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @Schema(
                description = "Email address of the account requesting a password reset.",
                example = "user@example.com"
        )
        @NotBlank(message = "{auth.email.required}", groups = FirstOrder.class)
        @ValidEmail(message = "{auth.email.invalid}", groups = SecondOrder.class)
        String email
) {}
