package io.github.amichailides.merimna.user.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class UserNotFoundByIdException extends BaseApplicationException {
    public UserNotFoundByIdException() {
        super(ErrorCode.USER_NOT_FOUND_BY_ID);
    }
}
