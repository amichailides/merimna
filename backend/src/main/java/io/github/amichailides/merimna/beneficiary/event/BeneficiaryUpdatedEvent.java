package io.github.amichailides.merimna.beneficiary.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.domain.Beneficiary;

import java.util.Map;
import java.util.UUID;

public record BeneficiaryUpdatedEvent(
        UUID beneficiaryPublicId,
        EntityChangeSet changeSet
) implements AuditableEvent {

    public static BeneficiaryUpdatedEvent from(Beneficiary beneficiary, EntityChangeSet changeSet) {
        return new BeneficiaryUpdatedEvent(beneficiary.getPublicId(),
                changeSet);
    }

    @Override
    public AuditAction action() {
        return AuditAction.BENEFICIARY_UPDATED;
    }

    @Override
    public UUID entityPublicId() {
        return beneficiaryPublicId;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "changes", changeSet.changes()
        );
    }
}
