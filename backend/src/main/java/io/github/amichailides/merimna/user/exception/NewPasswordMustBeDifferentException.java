package io.github.amichailides.merimna.user.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class NewPasswordMustBeDifferentException extends BaseApplicationException {
    public NewPasswordMustBeDifferentException() {
        super(ErrorCode.NEW_PASSWORD_MUST_BE_DIFFERENT);
    }
}
