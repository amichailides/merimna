package io.github.amichailides.merimna.security.refresh;

import io.github.amichailides.merimna.domain.User;

public record RefreshTokenRotationResult(
        User user,
        String newRawRefreshToken
) {}