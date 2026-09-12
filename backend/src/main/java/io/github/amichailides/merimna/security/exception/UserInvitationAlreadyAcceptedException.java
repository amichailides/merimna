package io.github.amichailides.merimna.security.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

public class UserInvitationAlreadyAcceptedException
        extends BaseApplicationException {

    public UserInvitationAlreadyAcceptedException() {
        super(ErrorCode.INVALID_USER_INVITATION);
    }
}