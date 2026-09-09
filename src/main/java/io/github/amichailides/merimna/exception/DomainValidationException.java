package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents field-based business/domain validation failures.
 *
 * <p>Accepts a field-to-message-key map and converts it to the
 * {@code Map<String, List<String>>} structure required by
 * {@link BaseValidationException}.</p>
 * <p>
 * TODO(#40): Align field-based domain validation HTTP statuses.
 * DomainValidationException currently resolves all field-based domain rules
 * through DOMAIN_RULE_VIOLATION, ignoring field-specific ErrorCode statuses.
 */
@Getter
public class DomainValidationException extends BaseValidationException {

    public DomainValidationException(Map<String, String> validationErrors) {
        super(
                ErrorCode.DOMAIN_RULE_VIOLATION,
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
