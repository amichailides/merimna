package io.github.amichailides.merimna.beneficiary.event;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditableEvent;
import io.github.amichailides.merimna.domain.Beneficiary;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record BeneficiaryDischargedEvent(
        UUID entityPublicId,
        LocalDate dischargeDate,
        String dischargeReason,
        UUID dischargedByEmployeePublicId)
        implements AuditableEvent {

    public static BeneficiaryDischargedEvent from(Beneficiary beneficiary) {
        return new BeneficiaryDischargedEvent(
                beneficiary.getPublicId(),
                beneficiary.getDischargeDate(),
                beneficiary.getDischargeReason(),
                beneficiary.getDischargedBy().getPublicId());
    }
    @Override
    public AuditAction action() {
        return AuditAction.BENEFICIARY_DISCHARGED;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
                "dischargeDate", dischargeDate.toString(),
                "dischargeReason", dischargeReason,
                "dischargedByEmployeePublicId", dischargedByEmployeePublicId.toString()
        );
    }
}
