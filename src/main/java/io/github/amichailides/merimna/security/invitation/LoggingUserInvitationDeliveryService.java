package io.github.amichailides.merimna.security.invitation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("!mailtrap-api")
public class LoggingUserInvitationDeliveryService
        implements UserInvitationDeliveryService {

    @Override
    public void sendInvitation(String email, String rawToken) {
        log.info(
                "User invitation generated for {}",
                email
        );
    }
}