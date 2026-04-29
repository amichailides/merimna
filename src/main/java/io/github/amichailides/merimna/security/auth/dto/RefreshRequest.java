package io.github.amichailides.merimna.security.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(

        @Schema(description = "Opaque refresh token issued at login")
        @NotBlank(message = "{auth.refreshToken.required}")
        String refreshToken
) {}
