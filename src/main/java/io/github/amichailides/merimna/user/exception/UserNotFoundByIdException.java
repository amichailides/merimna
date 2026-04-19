package io.github.amichailides.merimna.user.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

public class UserNotFoundByIdException extends BaseDomainException {
    public UserNotFoundByIdException() {
        super(ErrorCode.USER_NOT_FOUND_BY_ID);
    }
}
