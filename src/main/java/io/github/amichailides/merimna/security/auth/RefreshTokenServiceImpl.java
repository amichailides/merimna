package io.github.amichailides.merimna.security.auth;

import io.github.amichailides.merimna.domain.RefreshToken;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.security.RefreshTokenGenerator;
import io.github.amichailides.merimna.security.config.SecurityProperties;
import io.github.amichailides.merimna.security.exception.InvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityProperties securityProperties;

    @Transactional
    public String createRefreshToken(User user, String userAgent, String ipAddress) {
        String rawToken = refreshTokenGenerator.generate();

        String tokenHash = hash(rawToken);

        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(securityProperties.getRefreshToken().getExpiration());

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

    @Transactional(readOnly = true)
    public User validateAndGetUser(String rawRefreshToken) {
        String tokenHash = hash(rawRefreshToken);

        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!refreshToken.isUsable()) {
            throw new InvalidRefreshTokenException();
        }

        User user = refreshToken.getUser();

        if (!user.isActive()) {
            throw new InvalidRefreshTokenException();
        }

        return user;
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
