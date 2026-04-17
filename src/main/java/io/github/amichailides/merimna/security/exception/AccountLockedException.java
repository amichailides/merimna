package io.github.amichailides.merimna.security.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class AccountLockedException extends BaseDomainException {
    public AccountLockedException() {
        super(ErrorCode.ACCOUNT_LOCKED);
    }
}
