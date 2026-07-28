package io.github.amichailides.merimna.security.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class InvalidUserInvitationException extends BaseApplicationException {

    public InvalidUserInvitationException() {
        super(ErrorCode.INVALID_USER_INVITATION);
    }
}