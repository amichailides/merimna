package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public abstract class BaseValidationException extends BaseDomainException {

    private final Map<String, String> validationErrors;

    protected BaseValidationException(ErrorCode errorCode,
                                      Map<String, String> validationErrors,
                                      Object... args) {

        super(errorCode, args);
        this.validationErrors = validationErrors;
    }
}