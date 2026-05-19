package io.github.amichailides.merimna.security.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.security.refresh.RefreshToken;

import java.util.Map;
import java.util.UUID;

public record RefreshTokenReuseDetectedEvent(
        UUID userPublicId,
        UUID refreshTokenPublicId,
        UUID replacedByTokenPublicId
) implements AuditableEvent {

    public static RefreshTokenReuseDetectedEvent of(RefreshToken token) {
        return new RefreshTokenReuseDetectedEvent(
                token.getUser().getPublicId(),
                token.getPublicId(),
                token.getReplacedByTokenPublicId()
        );
    }

    @Override
    public AuditAction action() {
        return AuditAction.AUTH_REFRESH_TOKEN_REUSE_DETECTED;
    }

    @Override
    public UUID entityPublicId() {
        return userPublicId;
    }

    @Override
    public UUID actorUserPublicId() {
        return null;
    }

    @Override
    public UUID actorEmployeePublicId() {
        return null;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "refreshTokenPublicId", refreshTokenPublicId,
                "replacedByTokenPublicId", replacedByTokenPublicId
        );
    }
}
