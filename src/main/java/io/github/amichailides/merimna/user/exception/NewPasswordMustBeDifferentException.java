package io.github.amichailides.merimna.user.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class NewPasswordMustBeDifferentException extends BaseDomainException {
    public NewPasswordMustBeDifferentException() {
        super(ErrorCode.NEW_PASSWORD_MUST_BE_DIFFERENT);
    }
}
