package io.github.amichailides.merimna.security.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.User;

import java.util.UUID;

public record AuthPasswordChangedEvent(
        UUID userPublicId
) implements AuditableEvent {

    public static AuthPasswordChangedEvent from(User user) {
        return new AuthPasswordChangedEvent(user.getPublicId());
    }

    @Override
    public AuditAction action() {
        return AuditAction.AUTH_PASSWORD_CHANGED;
    }

    @Override
    public UUID entityPublicId() {
        return userPublicId;
    }
}
