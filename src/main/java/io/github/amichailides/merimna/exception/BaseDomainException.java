package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;

public abstract class BaseDomainException extends BaseApplicationException {

    protected BaseDomainException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}