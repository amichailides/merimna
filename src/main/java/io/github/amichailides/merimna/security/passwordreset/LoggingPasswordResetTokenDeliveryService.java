package io.github.amichailides.merimna.security.passwordreset;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("!mailtrap & !mailtrap-api")
public class LoggingPasswordResetTokenDeliveryService implements PasswordResetTokenDeliveryService {

    @Override
    public void sendPasswordResetToken(String email, String rawToken) {
        log.info("Password reset token generated for {}", email);
    }
}