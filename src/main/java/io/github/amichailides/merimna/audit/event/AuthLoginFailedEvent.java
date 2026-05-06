package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public record AuthLoginFailedEvent(
        String attemptedEmail,
        ErrorCode failureReason
) implements AuditableEvent {

    public static AuthLoginFailedEvent  from(String attemptedEmail, BaseDomainException ex){
        return new AuthLoginFailedEvent(attemptedEmail, ex.getErrorCode());
    }

    @Override
    public AuditAction action() {
        return AuditAction.AUTH_LOGIN_FAILED;
    }

    @Override
    public UUID entityPublicId() {
        return null;
    }

    @Override
    public Map<String, Object> metadata() {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("attemptedEmail", attemptedEmail);
        metadata.put("failureReason", failureReason.name());
        return metadata;
    }
}
