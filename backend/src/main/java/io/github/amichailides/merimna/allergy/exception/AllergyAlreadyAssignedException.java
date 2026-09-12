package io.github.amichailides.merimna.allergy.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

public class AllergyAlreadyAssignedException extends BaseDomainException {

    public AllergyAlreadyAssignedException(UUID currentBeneficiaryPublicId,
                                           UUID targetBeneficiaryPublicId) {
        super(ErrorCode.ALLERGY_ALREADY_ASSIGNED, Map.of(
                "currentBeneficiaryPublicId", currentBeneficiaryPublicId,
                "targetBeneficiaryPublicId", targetBeneficiaryPublicId
        ));
    }
}
