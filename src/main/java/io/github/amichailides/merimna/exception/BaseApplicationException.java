package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import lombok.Getter;
import java.util.Arrays;

@Getter
public abstract class BaseApplicationException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] args;

    protected BaseApplicationException(ErrorCode errorCode, Object... args) {
        super(String.format("%s - args=%s", errorCode.getCode(), Arrays.toString(args)));
        this.errorCode = errorCode;
        this.args = args;
    }
}
