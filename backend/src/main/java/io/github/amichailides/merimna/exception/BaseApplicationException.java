package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

@Getter
public abstract class BaseApplicationException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] args;
    private final Map<String, Object> context;

    protected BaseApplicationException(ErrorCode errorCode, Object... args) {
        super(String.format("%s - args=%s", errorCode.getCode(), Arrays.toString(args)));
        this.errorCode = errorCode;
        this.args = args;
        this.context = Map.of();
    }

    protected BaseApplicationException(ErrorCode errorCode, Map<String, Object> context) {
        super(String.format("%s - context=%s", errorCode.getCode(), context));
        this.errorCode = errorCode;
        this.args = new Object[0];
        this.context = context != null ? context : Map.of();
    }
}
