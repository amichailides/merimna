package io.github.amichailides.merimna.security.auth;

import io.github.amichailides.merimna.exception.BaseApplicationException;
import io.github.amichailides.merimna.security.event.AuthLoginFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthLoginFailureAuditService {

    private final ApplicationEventPublisher eventPublisher;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailure(
            String attemptedEmail,
            BaseApplicationException ex
    ) {
        eventPublisher.publishEvent(
                AuthLoginFailedEvent.from(attemptedEmail, ex)
        );
    }
}
