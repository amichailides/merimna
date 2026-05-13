package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.domain.Allergy;

import java.util.Map;
import java.util.UUID;

public record AllergyUpdatedEvent(
        UUID allergyPublicId,
        UUID beneficiaryPublicId,
        EntityChangeSet changeSet
) implements AuditableEvent {

    public static AllergyUpdatedEvent of(
            Allergy allergy,
            UUID beneficiaryPublicId,
            EntityChangeSet changeSet) {
        return new AllergyUpdatedEvent(
                allergy.getPublicId(),
                beneficiaryPublicId,
                changeSet
        );
    }

    @Override
    public AuditAction action () {
        return AuditAction.ALLERGY_UPDATED;
    }

    @Override
    public UUID entityPublicId() {
        return allergyPublicId;
    }
    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "beneficiaryPublicId", beneficiaryPublicId,
                "changes", changeSet.changes()
        );
    }
}
