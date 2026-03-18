package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Exception αποτυχίας σε business/domain validation κανόνες.
 *
 * <p>Δέχεται ένα απλό Map (πεδίο -> κλειδί μηνύματος) και το μετατρέπει
 * στη δομή Map<String, List<String>> που απαιτεί η {@link BaseValidationException},
 * ώστε να διατηρείται η συνέπεια στο API error response.</p>
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
