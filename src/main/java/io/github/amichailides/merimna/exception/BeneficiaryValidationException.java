package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import io.github.amichailides.merimna.model.Beneficiary;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Exception που υποδηλώνει αποτυχία σε business validation κανόνες
 * που αφορούν την οντότητα {@link Beneficiary}
 * ή τα συνδεδεμένα με αυτήν δεδομένα (π.χ. LegalRepresentative).
 * <p>
 * Δέχεται ένα απλό Map (πεδίο -> κλειδί μηνύματος) και το μετατρέπει στη δομή
 * Map<String, List<String>> που απαιτεί η {@link BaseValidationException},
 * ώστε να διατηρηθεί η συνέπεια στο API error response.
 * </p>
 */
@Getter
public class BeneficiaryValidationException extends BaseValidationException {
    public BeneficiaryValidationException(Map<String, String> validationErrors) {
        super(
                ErrorCode.BENEFICIARY_VALIDATION_FAILED,
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
