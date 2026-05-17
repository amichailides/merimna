package io.github.amichailides.merimna.security.refresh;

import io.github.amichailides.merimna.domain.User;

public interface RefreshTokenService {

    String createRefreshToken(User user, String userAgent, String ipAddress);

    void revokeToken(String rawToken);

    RefreshTokenRotationResult rotateToken(String rawRefreshToken, String userAgent, String ipAddress);
}
