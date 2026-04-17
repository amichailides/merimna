package io.github.amichailides.merimna.security.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AccountDisabledException extends BaseDomainException {
    public AccountDisabledException() {
        super(ErrorCode.ACCOUNT_DISABLED);
    }
}
