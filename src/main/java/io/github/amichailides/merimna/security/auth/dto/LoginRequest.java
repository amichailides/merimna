package io.github.amichailides.merimna.security.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Schema(description = "Email address of the user", example = "admin@merimna.local")
        @NotBlank(message = "{auth.email.required}")
        @Email(message = "{auth.email.invalid}")
        String email,

        @Schema(description = "Password of the user", example = "password123")
        @NotBlank(message = "{auth.password.required}")
        String password
) {}
