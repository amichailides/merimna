package io.github.amichailides.merimna.security.passwordreset;

import io.github.amichailides.merimna.domain.RevocationReason;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.security.config.SecurityProperties;
import io.github.amichailides.merimna.security.event.UserPasswordResetEvent;
import io.github.amichailides.merimna.security.exception.InvalidPasswordResetTokenException;
import io.github.amichailides.merimna.security.refresh.RefreshTokenRevocationService;
import io.github.amichailides.merimna.security.token.OpaqueTokenGenerator;
import io.github.amichailides.merimna.security.token.TokenHasher;
import io.github.amichailides.merimna.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final OpaqueTokenGenerator opaqueTokenGenerator;
    private final TokenHasher tokenHasher;
    private final SecurityProperties securityProperties;
    private final PasswordResetTokenDeliveryService deliveryService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRevocationService refreshTokenRevocationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> createAndSendResetToken(user, email));
    }

    private void createAndSendResetToken(User user, String email) {
        Instant now = Instant.now();

        String rawToken = opaqueTokenGenerator.generate();
        String tokenHash = tokenHasher.hash(rawToken);

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .tokenHash(tokenHash)
                .user(user)
                .expiresAt(now.plus(securityProperties.getPasswordReset().getExpiration()))
                .build();

        passwordResetTokenRepository.save(passwordResetToken);

        deliveryService.sendPasswordResetToken(email, rawToken);
    }

    @Override
    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        Instant now = Instant.now();

        String tokenHash = tokenHasher.hash(rawToken);

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidPasswordResetTokenException::new);

        if (!passwordResetToken.isValid(now)) {
            throw new InvalidPasswordResetTokenException();
        }

        User user = passwordResetToken.getUser();

        user.setEncodedPassword(passwordEncoder.encode(newPassword));

        passwordResetToken.markUsed(now);

        refreshTokenRevocationService.revokeAllActiveTokensForUser(
                user,
                RevocationReason.PASSWORD_RESET,
                now
        );

        eventPublisher.publishEvent(
                UserPasswordResetEvent.from(user));
    }
}

