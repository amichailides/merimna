package io.github.amichailides.merimna.security.invitation;

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
public class MailtrapApiUserInvitationDeliveryService
        implements UserInvitationDeliveryService {

    private final RestClient mailtrapRestClient;
    private final AppProperties appProperties;

    @Override
    public void sendInvitation(String email, String rawToken) {
        String invitationUrl =
                appProperties.getFrontend().getAcceptInvitationUrl()
                        + "?token=" + rawToken;

        Map<String, Object> requestBody = Map.of(
                "from", Map.of(
                        "email", appProperties.getMail().getFrom(),
                        "name", "Merimna"
                ),
                "to", List.of(
                        Map.of("email", email)
                ),
                "subject", "Ολοκλήρωση δημιουργίας λογαριασμού",
                "text", """
                        Έχει δημιουργηθεί πρόσκληση για τον λογαριασμό σας στο Merimna.

                        Για να ορίσετε όνομα χρήστη και κωδικό πρόσβασης, ανοίξτε τον παρακάτω σύνδεσμο:

                        %s
                        """.formatted(invitationUrl)
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