package io.github.amichailides.merimna.user.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

import java.util.Map;
import java.util.UUID;

public class UserNotFoundByPublicIdException extends BaseApplicationException {
    public UserNotFoundByPublicIdException(UUID userPublicId) {
        super(ErrorCode.USER_NOT_FOUND_BY_PUBLIC_ID, Map.of(
                "userPublicId", userPublicId
        ));
    }
}
