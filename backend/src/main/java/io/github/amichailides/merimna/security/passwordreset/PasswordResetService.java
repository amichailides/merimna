package io.github.amichailides.merimna.security.passwordreset;

public interface PasswordResetService {

    void requestPasswordReset(String email);

    void resetPassword(String rawToken, String newPassword);
}