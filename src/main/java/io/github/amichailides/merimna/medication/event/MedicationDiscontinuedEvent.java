package io.github.amichailides.merimna.medication.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.Medication;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record MedicationDiscontinuedEvent(
        UUID medicationPublicId,
        UUID beneficiaryPublicId,
        String medicationName,
        LocalDate endedAt
) implements AuditableEvent {

    public static MedicationDiscontinuedEvent of(
            Medication medication,
            UUID beneficiaryPublicId
    ) {
        return new MedicationDiscontinuedEvent(
                medication.getPublicId(),
                beneficiaryPublicId,
                medication.getName(),
                medication.getEndedAt()
        );
    }

    @Override
    public AuditAction action() {
        return AuditAction.MEDICATION_DISCONTINUED;
    }

    @Override
    public UUID entityPublicId() {
        return medicationPublicId;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "beneficiaryPublicId", beneficiaryPublicId,
                "medicationName", medicationName,
                "endedAt", endedAt
        );
    }
}