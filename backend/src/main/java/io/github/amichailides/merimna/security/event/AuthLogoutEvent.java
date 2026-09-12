package io.github.amichailides.merimna.security.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditContext;
import io.github.amichailides.merimna.audit.AuditableEvent;

import java.util.UUID;

public record AuthLogoutEvent(
        UUID userPublicId
) implements AuditableEvent {

    public static AuthLogoutEvent from(AuditContext auditContext) {
        return new AuthLogoutEvent(auditContext.getUserPublicId());
    }

    @Override
    public UUID entityPublicId() {
        return userPublicId;
    }

    @Override
    public AuditAction action() {
        return AuditAction.AUTH_LOGOUT;
    }
}
