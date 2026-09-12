package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class BeneficiaryInactiveException extends BaseDomainException {
    public BeneficiaryInactiveException(UUID publicId) {
        super(ErrorCode.BENEFICIARY_INACTIVE, Map.of(
                "beneficiaryPublicId", publicId
        ));
    }
}
