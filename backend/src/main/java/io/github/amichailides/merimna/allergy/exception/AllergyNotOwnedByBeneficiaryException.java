package io.github.amichailides.merimna.allergy.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class AllergyNotOwnedByBeneficiaryException extends BaseDomainException {
    public AllergyNotOwnedByBeneficiaryException(UUID allergyPublicId, UUID beneficiaryPublicId) {
        super(ErrorCode.ALLERGY_NOT_OWNED_BY_BENEFICIARY, Map.of(
                "allergyPublicId", allergyPublicId,
                "beneficiaryPublicId", beneficiaryPublicId
        ));
    }
}
