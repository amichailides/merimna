package io.github.amichailides.merimna.security.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class AccountLockedException extends BaseApplicationException {
    public AccountLockedException() {
        super(ErrorCode.ACCOUNT_LOCKED);
    }
}
