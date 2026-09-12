package io.github.amichailides.merimna.security.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class AccountDisabledException extends BaseApplicationException {
    public AccountDisabledException() {
        super(ErrorCode.ACCOUNT_DISABLED);
    }
}
