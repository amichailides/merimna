package io.github.amichailides.merimna.security.invitation;

import io.github.amichailides.merimna.security.event.UserInvitationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInvitationEventListener {

    private final UserInvitationDeliveryService deliveryService;

    @Async("emailTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserInvitationCreatedEvent event) {
        try {
            deliveryService.sendInvitation(
                    event.email(),
                    event.rawToken()
            );
        } catch (Exception exception) {
            log.error(
                    "Failed to deliver user invitation email to {}",
                    event.email(),
                    exception
            );
        }
    }
}