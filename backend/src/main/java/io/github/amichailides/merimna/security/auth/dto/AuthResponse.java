package io.github.amichailides.merimna.security.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthResponse(
        @Schema(
                description = "Short-lived JWT access token",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String accessToken,

        @Schema(
                description = "Long-lived opaque refresh token",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String refreshToken
) {}