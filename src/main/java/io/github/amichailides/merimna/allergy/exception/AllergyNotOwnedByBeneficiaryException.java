package io.github.amichailides.merimna.allergy.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AllergyNotOwnedByBeneficiaryException extends BaseDomainException {
    public AllergyNotOwnedByBeneficiaryException(Long allergyId, Long beneficiaryId) {

        super(ErrorCode.ALLERGY_NOT_OWNED_BY_BENEFICIARY,
                allergyId,
                beneficiaryId);
    }
}
