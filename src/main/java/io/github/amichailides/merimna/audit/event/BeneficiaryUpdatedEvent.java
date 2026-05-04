package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.Beneficiary;

import java.util.Map;
import java.util.UUID;

public record BeneficiaryUpdatedEvent(
        UUID beneficiaryPublicId
) implements AuditableEvent {

    public static BeneficiaryUpdatedEvent from(Beneficiary beneficiary) {
        return new BeneficiaryUpdatedEvent(beneficiary.getPublicId());
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
                "operation", "PROFILE_UPDATE_REQUEST"
        );
    }
}
