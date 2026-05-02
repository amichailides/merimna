package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;

import java.util.UUID;

public record BeneficiaryCreatedEvent(
        UUID entityPublicId
) implements AuditableEvent {
    @Override
    public AuditAction action() {
        return AuditAction.BENEFICIARY_CREATED;
    }
}
