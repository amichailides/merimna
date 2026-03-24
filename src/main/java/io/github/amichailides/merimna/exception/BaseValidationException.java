package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import lombok.Getter;
import java.util.List;
import java.util.Map;

/**
 * Όλα τα validation errors αποθηκεύονται ως Map<String, List<String>>
 * για consistency με το API response schema.
 * Τα subclasses που έχουν simple Map<String, String> κάνουν convert στον constructor.
 */
@Getter
public abstract class BaseValidationException extends BaseDomainException {

    private final Map<String, List<String>> validationErrors;

    protected BaseValidationException(ErrorCode errorCode,
                                      Map<String, List<String>> validationErrors) {

        super(errorCode);
        this.validationErrors = validationErrors;
    }
}