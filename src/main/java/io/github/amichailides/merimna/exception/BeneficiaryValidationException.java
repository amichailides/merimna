package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import lombok.Getter;

import java.util.Map;

/**
 * Φάση: "Τα δεδομένα που έστειλες έχουν θέματα σε διάφορα σημεία (π.χ. ΑΜΚΑ, Ηλικία κτλ)".
 * Κληρονομεί από την BaseValidationException γιατί κουβαλάει το Map
 * με όλα τα "παράπονα" του Validator.
 */
@Getter
public class BeneficiaryValidationException extends BaseValidationException {
    public BeneficiaryValidationException(Map<String, String> validationErrors) {
        super(
                ErrorCode.BENEFICIARY_VALIDATION_FAILED,
                validationErrors
        );
    }

}
