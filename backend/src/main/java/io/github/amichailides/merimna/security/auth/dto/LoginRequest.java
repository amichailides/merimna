package io.github.amichailides.merimna.security.auth.dto;

import io.github.amichailides.merimna.validation.annotations.ValidEmail;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Schema(description = "Email address of the user", example = "admin@merimna.local")
        @NotBlank(message = "{auth.email.required}", groups = FirstOrder.class)
        @ValidEmail(message = "{auth.email.invalid}", groups = SecondOrder.class)
        String email,

        @Schema(description = "Password of the user", example = "password123")
        @NotBlank(message = "{auth.password.required}", groups = FirstOrder.class)
        String password
) {}