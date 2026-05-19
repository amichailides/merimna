package io.github.amichailides.merimna.security.refresh;

import io.github.amichailides.merimna.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenRevocationService {

    private final RefreshTokenRepository refreshTokenRepository;

    public int revokeAllActiveTokensForUser(
            User user,
            RevocationReason reason,
            Instant revokedAt
    ) {
        return refreshTokenRepository.revokeAllActiveTokensForUser(
                user.getId(),
                reason,
                revokedAt
        );
    }
}
