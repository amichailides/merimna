package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class MedicationNotOwnedByBeneficiaryException extends BaseBusinessException {
    public MedicationNotOwnedByBeneficiaryException(Long medicationId, Long beneficiaryId) {

        super(ErrorCode.MEDICATION_NOT_OWNED_BY_BENEFICIARY,
                HttpStatus.CONFLICT,
                ErrorCode.MEDICATION_NOT_OWNED_BY_BENEFICIARY.getMessageKey(),
                medicationId,
                beneficiaryId);
    }
}
