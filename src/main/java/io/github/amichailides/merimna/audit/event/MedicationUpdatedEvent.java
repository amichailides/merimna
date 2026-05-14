package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.domain.Medication;

import java.util.Map;
import java.util.UUID;

public record MedicationUpdatedEvent(
        UUID medicationPublicId,
        UUID beneficiaryPublicId,
        EntityChangeSet changeSet
) implements AuditableEvent {

    public static MedicationUpdatedEvent of(
            Medication medication,
            UUID beneficiaryPublicId,
            EntityChangeSet changeSet
    ) {
        return new MedicationUpdatedEvent(
                medication.getPublicId(),
                beneficiaryPublicId,
                changeSet
        );
    }

    @Override
    public AuditAction action() {
        return AuditAction.MEDICATION_UPDATED;
    }

    @Override
    public UUID entityPublicId() {
        return medicationPublicId;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "beneficiaryPublicId", beneficiaryPublicId,
                "changes", changeSet.changes()
        );
    }
}
