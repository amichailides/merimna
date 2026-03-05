package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;

import java.util.Map;

/**
 * Ο "Ελεγκτής" της Φόρμας (422 Unprocessable Entity).
 * Φάση: "Τα δεδομένα που έστειλες έχουν θέματα σε διάφορα σημεία (π.χ. ΑΜΚΑ, Ηλικία κτλ)".
 * Κληρονομεί από την BaseValidationException γιατί κουβαλάει το Map
 * με όλα τα "παράπονα" του Validator.
 */
public class BeneficiaryValidationException extends BaseValidationException {
    public BeneficiaryValidationException(Map<String, String> errors) {
        super(
                ErrorCode.BENEFICIARY_VALIDATION_FAILED,
                errors // Το πακέτο με τα λάθη
        );
    }
}
