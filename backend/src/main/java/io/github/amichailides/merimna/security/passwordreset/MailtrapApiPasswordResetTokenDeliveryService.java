package io.github.amichailides.merimna.security.passwordreset;

import io.github.amichailides.merimna.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@Profile("mailtrap-api")
@RequiredArgsConstructor
public class MailtrapApiPasswordResetTokenDeliveryService
        implements PasswordResetTokenDeliveryService {

    private final RestClient mailtrapRestClient;
    private final AppProperties appProperties;

    @Override
    public void sendPasswordResetToken(String email, String rawToken) {
        String resetUrl =
                appProperties.getFrontend().getResetPasswordUrl()
                        + "?token=" + rawToken;

        Map<String, Object> requestBody = Map.of(
                "from", Map.of(
                        "email", appProperties.getMail().getFrom(),
                        "name", "Merimna"
                ),
                "to", List.of(
                        Map.of("email", email)
                ),
                "subject", "Reset your Merimna password",
                "text", """
                        We received a request to reset your Merimna password.

                        Use the link below to set a new password:

                        %s

                        If you did not request this, you can ignore this email.
                        """.formatted(resetUrl)
        );

        mailtrapRestClient
                .post()
                .uri("/api/send")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }
}