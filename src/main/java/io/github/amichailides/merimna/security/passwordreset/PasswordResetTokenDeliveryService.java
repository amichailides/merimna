package io.github.amichailides.merimna.security.passwordreset;

public interface PasswordResetTokenDeliveryService {

    void sendPasswordResetToken(String email, String rawToken);
}
