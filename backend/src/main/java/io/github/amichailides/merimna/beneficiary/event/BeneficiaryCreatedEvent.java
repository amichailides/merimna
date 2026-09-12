package io.github.amichailides.merimna.beneficiary.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.Beneficiary;

import java.util.UUID;

public record BeneficiaryCreatedEvent(
        UUID beneficiaryPublicId
) implements AuditableEvent {

    public static BeneficiaryCreatedEvent from(Beneficiary beneficiary) {
        return new BeneficiaryCreatedEvent(beneficiary.getPublicId());
    }

    @Override
    public AuditAction action() {
        return AuditAction.BENEFICIARY_CREATED;
    }

    @Override
    public UUID entityPublicId() {
        return beneficiaryPublicId;
    }
}
