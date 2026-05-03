package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;

import java.util.Map;
import java.util.UUID;

public record BeneficiaryDischargedEvent(
        UUID entityPublicId
) implements AuditableEvent {

    @Override
    public AuditAction action() {
        return AuditAction.BENEFICIARY_DISCHARGED;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "activeAfter", false
        );
    }
}
