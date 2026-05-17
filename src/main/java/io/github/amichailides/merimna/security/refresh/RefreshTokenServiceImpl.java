package io.github.amichailides.merimna.security.refresh;

import io.github.amichailides.merimna.domain.RefreshToken;
import io.github.amichailides.merimna.domain.RevocationReason;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.security.config.SecurityProperties;
import io.github.amichailides.merimna.security.exception.InvalidRefreshTokenException;
import io.github.amichailides.merimna.security.token.OpaqueTokenGenerator;
import io.github.amichailides.merimna.security.token.TokenHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final OpaqueTokenGenerator opaqueTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityProperties securityProperties;
    private final RefreshTokenReuseDetectionService refreshTokenReuseDetectionService;
    private final TokenHasher tokenHasher;

    @Override
    @Transactional
    public String createRefreshToken(User user, String userAgent, String ipAddress) {
        String rawToken = opaqueTokenGenerator.generate();

        String tokenHash = tokenHasher.hash(rawToken);

        Instant now = Instant.now();
        Instant expiresAt = now.plus(securityProperties.getRefreshToken().getExpiration());

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(tokenHash)
                .user(user)
                .expiresAt(expiresAt)
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .build();

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    @Override
    @Transactional
    public RefreshTokenRotationResult rotateToken(String rawRefreshToken, String userAgent, String ipAddress) {
        Instant now = Instant.now();

        String tokenHash = tokenHasher.hash(rawRefreshToken);

        RefreshToken existing = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        ensureCanRotate(existing, now);

        String newRawToken = opaqueTokenGenerator.generate();

        RefreshToken newToken = RefreshToken.builder()
                .tokenHash(tokenHasher.hash(newRawToken))
                .user(existing.getUser())
                .expiresAt(now.plus(securityProperties.getRefreshToken().getExpiration()))
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .build();

        refreshTokenRepository.save(newToken);

        existing.revoke(RevocationReason.ROTATED, newToken.getPublicId());

        return new RefreshTokenRotationResult(existing.getUser(), newRawToken);
    }

    private void ensureCanRotate(RefreshToken existing, Instant now) {
        if (existing.isRevoked()) {
            if (existing.wasRotated()) {
                refreshTokenReuseDetectionService.handleReuseDetected(existing, now);
            }

            throw new InvalidRefreshTokenException();
        }

        if (existing.isExpired()) {
            throw new InvalidRefreshTokenException();
        }

        if (!existing.getUser().isActive()) {
            throw new InvalidRefreshTokenException();
        }
    }

    @Override
    @Transactional
    public void revokeToken(String rawRefreshToken) {
        String refreshTokenHash = tokenHasher.hash(rawRefreshToken);

        refreshTokenRepository.findByTokenHash(refreshTokenHash)
                .ifPresent(token -> token.revoke(RevocationReason.LOGOUT));
    }
}
