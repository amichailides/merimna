package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Ο "Συλλέκτης" Σφαλμάτων.
 * Επέκταση της Base, αλλά με μια σημαντική διαφορά: Κουβαλάει ένα Map.
 * Τη χρησιμοποιούμε όταν θέλουμε να στείλουμε "Αναφορά Σφαλμάτων" για πολλά πεδία.
 * Αντί να σκάμε στην πρώτη αναποδιά, μαζεύουμε τα λάθη και τα στέλνουμε όλα μαζί.
 */
@Getter
public abstract class BaseValidationException extends BaseBusinessException {
    private final Map<String, String> validationErrors;

    protected BaseValidationException(ErrorCode code, HttpStatus status, String key, Map<String, String> errors) {
        super(code, status, key);
        this.validationErrors = errors;
    }
}
