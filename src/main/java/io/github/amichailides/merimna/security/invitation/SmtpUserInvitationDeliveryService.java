package io.github.amichailides.merimna.security.invitation;

import io.github.amichailides.merimna.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("mailtrap")
@RequiredArgsConstructor
public class SmtpUserInvitationDeliveryService
        implements UserInvitationDeliveryService {

    private final JavaMailSender mailSender;
    private final AppProperties appProperties;

    @Override
    public void sendInvitation(String email, String rawToken) {
        String invitationLink =
                appProperties.getFrontend().getAcceptInvitationUrl()
                        + "?token=" + rawToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(appProperties.getMail().getFrom());
        message.setTo(email);
        message.setSubject("Complete your Merimna account setup");
        message.setText("""
                You have been invited to access Merimna.

                Use the link below to create your username and password:
                %s

                If you were not expecting this invitation, you can ignore this email.
                """.formatted(invitationLink));

        mailSender.send(message);
    }
}