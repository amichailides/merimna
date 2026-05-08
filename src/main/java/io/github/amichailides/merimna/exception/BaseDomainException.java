package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;

import java.util.Map;

public abstract class BaseDomainException extends BaseApplicationException {

    protected BaseDomainException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    protected BaseDomainException(ErrorCode errorCode, Map<String, Object> context) {
        super(errorCode, context);
    }
}