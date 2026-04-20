package io.github.amichailides.merimna.user.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class InvalidCurrentPasswordException extends BaseDomainException {
    public InvalidCurrentPasswordException() {
        super(ErrorCode.INVALID_CURRENT_PASSWORD);
    }
}
