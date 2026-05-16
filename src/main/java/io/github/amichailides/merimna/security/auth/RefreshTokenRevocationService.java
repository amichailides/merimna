package io.github.amichailides.merimna.security.auth;

import io.github.amichailides.merimna.domain.RevocationReason;
import io.github.amichailides.merimna.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenRevocationService {

    private final RefreshTokenRepository refreshTokenRepository;

    // Uses a separate transaction so reuse-detection revocations survive the exception thrown by the refresh flow.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void revokeAllActiveTokensForReuseDetection(User user, Instant now) {
        refreshTokenRepository.revokeAllActiveTokensForUser(
                user.getId(),
                RevocationReason.REUSE_DETECTED,
                now
        );
    }
}