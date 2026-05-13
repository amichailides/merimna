package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.User;

import java.util.UUID;

public record UserCreatedEvent(
        UUID userPublicId
) implements AuditableEvent {

    public static UserCreatedEvent from(User user) {
        return new UserCreatedEvent(user.getPublicId());
    }

    @Override
    public AuditAction action() {
        return AuditAction.USER_CREATED;
    }

    @Override
    public UUID entityPublicId() {
        return userPublicId;
    }
}
