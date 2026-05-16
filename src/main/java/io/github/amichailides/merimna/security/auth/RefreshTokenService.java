package io.github.amichailides.merimna.security.auth;

import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.security.auth.dto.RefreshTokenRotationResult;

public interface RefreshTokenService {

    String createRefreshToken(User user, String userAgent, String ipAddress);

    void revokeToken(String rawToken);

    RefreshTokenRotationResult rotateToken(String rawRefreshToken, String userAgent, String ipAddress);
}
