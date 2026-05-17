package io.github.amichailides.merimna.security.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class InvalidPasswordResetTokenException extends BaseApplicationException {
    public InvalidPasswordResetTokenException() {
        super(ErrorCode.INVALID_PASSWORD_RESET_TOKEN);
    }
}
