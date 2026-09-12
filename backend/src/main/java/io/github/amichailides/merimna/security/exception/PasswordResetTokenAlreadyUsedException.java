package io.github.amichailides.merimna.security.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class PasswordResetTokenAlreadyUsedException extends BaseApplicationException {
    public PasswordResetTokenAlreadyUsedException() {
        super(ErrorCode.INVALID_PASSWORD_RESET_TOKEN);
    }
}
