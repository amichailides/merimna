package io.github.amichailides.merimna.security.auth.dto;

import io.github.amichailides.merimna.domain.User;

public record RefreshTokenRotationResult(
        User user,
        String newRawRefreshToken
) {}