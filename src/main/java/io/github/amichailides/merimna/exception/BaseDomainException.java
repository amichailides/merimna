package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import lombok.Getter;

@Getter
public class BaseDomainException extends RuntimeException {
    private final ErrorCode errorCode;

    protected BaseDomainException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
