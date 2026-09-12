package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ConflictValidationException extends BaseValidationException {
    public ConflictValidationException(Map<String, String> validationErrors) {
        super(
                ErrorCode.RESOURCE_CONFLICT,
                validationErrors.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> List.of(entry.getValue()),
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        ))
        );
    }
}
