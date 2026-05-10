package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.domain.User;

import java.util.Map;
import java.util.UUID;

public record UserUpdatedEvent(
        UUID publicId,
        EntityChangeSet changeSet
) implements AuditableEvent {

    public static UserUpdatedEvent from(User user, EntityChangeSet changeSet) {
        return new UserUpdatedEvent(
                user.getPublicId(),
                changeSet
        );
    }

    @Override
    public AuditAction action() {
        return AuditAction.USER_UPDATED;
    }
    @Override
    public UUID entityPublicId() {
        return publicId;
    }
    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "changes", changeSet.changes()
        );
    }
}
