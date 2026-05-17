package io.github.amichailides.merimna.security;

import io.github.amichailides.merimna.domain.RefreshToken;
import io.github.amichailides.merimna.domain.RevocationReason;
import io.github.amichailides.merimna.security.auth.RefreshTokenRepository;
import io.github.amichailides.merimna.security.event.RefreshTokenReuseDetectedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenReuseDetectionService {

    private final ApplicationEventPublisher eventPublisher;
    private final RefreshTokenRepository refreshTokenRepository;

    // Uses a separate transaction so reuse-detection revocations and audit survive
    // the exception thrown by the refresh flow.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleReuseDetected(RefreshToken reusedToken, Instant now) {
        refreshTokenRepository.revokeAllActiveTokensForUser(
                reusedToken.getUser().getId(),
                RevocationReason.REUSE_DETECTED,
                now
        );

        eventPublisher.publishEvent(
                RefreshTokenReuseDetectedEvent.of(reusedToken)
        );
    }
}