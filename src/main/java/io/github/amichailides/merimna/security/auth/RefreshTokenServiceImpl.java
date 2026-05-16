package io.github.amichailides.merimna.security.auth;

import io.github.amichailides.merimna.domain.RefreshToken;
import io.github.amichailides.merimna.domain.RevocationReason;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.security.RefreshTokenGenerator;
import io.github.amichailides.merimna.security.auth.dto.RefreshTokenRotationResult;
import io.github.amichailides.merimna.security.config.SecurityProperties;
import io.github.amichailides.merimna.security.exception.InvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityProperties securityProperties;
    private final RefreshTokenRevocationService refreshTokenRevocationService;

    @Override
    @Transactional
    public String createRefreshToken(User user, String userAgent, String ipAddress) {
        String rawToken = refreshTokenGenerator.generate();

        String tokenHash = hash(rawToken);

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

        String tokenHash = hash(rawRefreshToken);

        RefreshToken existing = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        ensureCanRotate(existing, now);

        String newRawToken = refreshTokenGenerator.generate();

        RefreshToken newToken = RefreshToken.builder()
                .tokenHash(hash(newRawToken))
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
                refreshTokenRevocationService.revokeAllActiveTokensForReuseDetection(
                        existing.getUser(),
                        now
                );
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
        String refreshTokenHash = hash(rawRefreshToken);

        refreshTokenRepository.findByTokenHash(refreshTokenHash )
                .ifPresent(token -> token.revoke(RevocationReason.LOGOUT));
    }

    private String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
