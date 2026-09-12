package io.github.amichailides.merimna.security.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.User;

import java.util.UUID;

public record UserPasswordResetEvent(
        UUID userPublicId,
        UUID employeePublicId
) implements AuditableEvent {
    public static UserPasswordResetEvent from(User user) {
        return new UserPasswordResetEvent(
                user.getPublicId(),
                user.getEmployee().getPublicId()
        );
    }

    @Override
    public AuditAction action() {
        return AuditAction.AUTH_PASSWORD_RESET;
    }

    @Override
    public UUID entityPublicId() {
        return userPublicId;
    }
}
