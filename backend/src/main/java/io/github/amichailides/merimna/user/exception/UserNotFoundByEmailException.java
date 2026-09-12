package io.github.amichailides.merimna.user.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class UserNotFoundByEmailException extends BaseApplicationException {
    public UserNotFoundByEmailException() {
        super(ErrorCode.USER_NOT_FOUND_BY_EMAIL);
    }
}
