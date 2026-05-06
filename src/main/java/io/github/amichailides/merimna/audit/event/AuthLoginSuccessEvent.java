package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.User;

import java.util.UUID;

public record AuthLoginSuccessEvent(
        UUID userPublicId,
        UUID employeePublicId
) implements AuditableEvent {

    public static AuthLoginSuccessEvent from(User user) {
        return new AuthLoginSuccessEvent(
                user.getPublicId(),
                user.getEmployee().getPublicId()
        );
    }

    @Override
    public UUID entityPublicId() {
        return userPublicId;
    }

    @Override
    public UUID actorUserPublicId() {
        return userPublicId;
    }

    @Override
    public UUID actorEmployeePublicId() {
        return employeePublicId;
    }

    @Override
    public AuditAction action() {
        return AuditAction.AUTH_LOGIN_SUCCESS;
    }
}