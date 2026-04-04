package io.github.amichailides.merimna.beneficiary.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class BeneficiaryInactiveException extends BaseDomainException {
    public BeneficiaryInactiveException(Long id) {
        super(ErrorCode.BENEFICIARY_INACTIVE, id);
    }
}
