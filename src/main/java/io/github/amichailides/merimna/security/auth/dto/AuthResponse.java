package io.github.amichailides.merimna.security.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
        @Schema(description = "Short-lived JWT access token")
        String accessToken,

        @Schema(description = "Long-lived opaque refresh token")
        String refreshToken
) {}
