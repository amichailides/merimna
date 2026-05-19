package io.github.amichailides.merimna.security.passwordreset;

import io.github.amichailides.merimna.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("mailtrap")
@RequiredArgsConstructor
public class SmtpPasswordResetTokenDeliveryService implements PasswordResetTokenDeliveryService {

    private final JavaMailSender mailSender;
    private final AppProperties appProperties;

    @Override
    public void sendPasswordResetToken(String email, String rawToken) {
        String resetLink = appProperties.getFrontend().getResetPasswordUrl()
                + "?token=" + rawToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(appProperties.getMail().getFrom());
        message.setTo(email);
        message.setSubject("Reset your Merimna password");
        message.setText("""
                We received a request to reset your Merimna password.

                Use the link below to set a new password:
                %s

                If you did not request this, you can ignore this email.
                """.formatted(resetLink));

        mailSender.send(message);
    }
}