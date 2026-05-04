package io.github.amichailides.merimna.audit.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.HouseUnit;

import java.util.Map;
import java.util.UUID;

public record BeneficiaryHouseUnitChangedEvent(
        UUID beneficiaryPublicId,
        UUID fromHouseUnitPublicId,
        UUID toHouseUnitPublicId
) implements AuditableEvent {

    public static BeneficiaryHouseUnitChangedEvent from(
            Beneficiary beneficiary,
            HouseUnit fromHouseUnit,
            HouseUnit toHouseUnit) {
        return new BeneficiaryHouseUnitChangedEvent(
                beneficiary.getPublicId(),
                fromHouseUnit.getPublicId(),
                toHouseUnit.getPublicId()
        );
    }

    @Override
    public AuditAction action() {
        return AuditAction.BENEFICIARY_HOUSE_UNIT_CHANGED;
    }

    @Override
    public UUID entityPublicId() {
        return beneficiaryPublicId;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "fromHouseUnitPublicId", fromHouseUnitPublicId.toString(),
                "toHouseUnitPublicId", toHouseUnitPublicId.toString()
        );
    }
}
