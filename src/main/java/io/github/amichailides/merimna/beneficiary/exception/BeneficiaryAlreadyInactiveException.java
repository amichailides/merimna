package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class BeneficiaryAlreadyInactiveException extends BaseDomainException {
    public BeneficiaryAlreadyInactiveException(UUID beneficiaryPublicId) {
        super(ErrorCode.BENEFICIARY_ALREADY_INACTIVE, Map.of(
                "beneficiaryPublicId", beneficiaryPublicId
        ));
    }
}
