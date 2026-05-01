package io.github.amichailides.merimna.security.auth;

import io.github.amichailides.merimna.domain.RevocationReason;
import io.github.amichailides.merimna.domain.User;

public interface RefreshTokenService {

    String createRefreshToken(User user, String userAgent, String ipAddress);

    User validateAndGetUser(String rawRefreshToken);

    void revokeToken(String rawToken);

    void revokeAllUserTokens(User user, RevocationReason reason);
}
