package io.github.amichailides.merimna.security.invitation;

import io.github.amichailides.merimna.security.event.UserInvitationCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserInvitationEventListener {

    private final UserInvitationDeliveryService deliveryService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserInvitationCreatedEvent event) {
        deliveryService.sendInvitation(
                event.email(),
                event.rawToken()
        );
    }
}