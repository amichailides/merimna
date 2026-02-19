package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseBusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus status;
    private final String messageKey;
    private final Object[] messageArgs;

    protected BaseBusinessException(
            ErrorCode errorCode,
            HttpStatus status,
            String messageKey,
            Object... messageArgs) {

        // Το κλειδί του μηνύματος ως «ταυτότητα» του exception στην κονσόλα και στα logs.
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.status = status;
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }
}