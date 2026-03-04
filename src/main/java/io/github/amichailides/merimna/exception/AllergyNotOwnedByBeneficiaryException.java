package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class AllergyNotOwnedByBeneficiaryException extends BaseBusinessException {
    public AllergyNotOwnedByBeneficiaryException(Long allergyId, Long beneficiaryId) {

        super(ErrorCode.ALLERGY_NOT_OWNED_BY_BENEFICIARY,
                HttpStatus.CONFLICT,
                ErrorCode.ALLERGY_NOT_OWNED_BY_BENEFICIARY.getMessageKey(),
                allergyId,
                beneficiaryId);
    }
}
