package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ApiResponse;
import io.github.amichailides.merimna.common.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Κεντρικός διαχειριστής εξαιρέσεων.
 * <p>Μετατρέπει τα exceptions σε {@link ApiResponse} χρησιμοποιώντας
 * τους προκαθορισμένους κωδικούς του {@link ErrorCode}.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    // Constructor injection
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BaseBusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BaseBusinessException ex,
            HttpServletRequest request) {

        // Παίρνουμε το localized message
        String message = translate(ex.getMessageKey(), ex.getMessageArgs());

        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.error(
                        ex.getErrorCode(),
                        ex.getStatus().value(),
                        ex.getStatus().getReasonPhrase(),
                        message,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(BaseValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            BaseValidationException ex,
            HttpServletRequest request) {

        String generalMessage = translate(ex.getMessageKey());

        // Μετατροπή των error keys σε ανθρώπινα μηνύματα (i18n)
        Map<String, String> localizedErrors = ex.getValidationErrors().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> translate(entry.getValue()),
                        (existing, replacement) -> existing + " | " + replacement,
                        LinkedHashMap::new         // Διατήρηση της σειράς (Insertion Order)
                ));

        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.validationError(
                        ex.getErrorCode(),
                        ex.getStatus().value(),
                        generalMessage,
                        localizedErrors,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        // Log το error για να δούμε τι έγινε στην DB
        log.error("Database integrity violation: {}", ex.getMostSpecificCause().getMessage());

        String message = messageSource.getMessage(
                "error.database.conflict",
                null,
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        ErrorCode.DATABASE_ERROR,
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        message,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        // 1. Μαζεύουμε τα Field Errors (lastName, amka κλπ)
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.merge(error.getField(),
                    error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                    (existing, replacement) -> existing + " | " + replacement);
        });

        // 2. ΜΑΖΕΥΟΥΜΕ ΤΑ GLOBAL ERRORS (Εδώ κρύβεται το EmergencyContact!)
        ex.getBindingResult().getGlobalErrors().forEach(error -> {
            // Η Spring για τα nested objects χρησιμοποιεί το όνομα του πεδίου
            // ή το όνομα της κλάσης (emergencyContactDTO). Το καθαρίζουμε:
            String key = error.getObjectName().replace("DTO", "");

            // Αν το σφάλμα αφορά το nested αντικείμενο, το βάζουμε στο Map
            errors.merge(key,
                    error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                    (existing, replacement) -> existing + " | " + replacement);
        });

        log.warn("Validation failed for {}: {}", request.getRequestURI(), errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.validationError(
                        ErrorCode.VALIDATION_FAILED,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        errors,
                        request.getRequestURI()
                                ));
    }

    // TODO: [Refactor] Εδώ μπορούμε να αναβαθμίσουμε τον Handler
    // χρησιμοποιώντας το InvalidFormatException του Jackson.
    // Σκοπός: Να εξάγουμε το συγκεκριμένο πεδίο (fieldName) και την άκυρη τιμή (invalidValue)
    // ώστε το μήνυμα να λέει: "Η τιμή 'abc' δεν είναι έγκυρη για το πεδίο 'houseUnit'".
    // Χρήσιμα tools: formatException.getPath(), formatException.getValue().
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.error("JSON parse error: {}", ex.getMessage());
        String message = "Μη έγκυρη μορφή δεδομένων στο σώμα του αιτήματος.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ErrorCode.INVALID_INPUT,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        message,
                        request.getRequestURI()));
    }

    // Για λάθη στο URL (π.χ. /api/beneficiaries/abc αντί για ID 123) -
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String propertyName = ex.getName();
        Object providedValue = ex.getValue();

        log.error("Type mismatch for parameter '{}': {}", propertyName, providedValue);

        String message = String.format("Η τιμή '%s' δεν είναι έγκυρη για την παράμετρο '%s'.",
                providedValue, propertyName);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ErrorCode.INVALID_INPUT,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        message,
                        request.getRequestURI()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

// Χρησιμοποιούμε LinkedHashMap για να κρατήσουμε τη σειρά των σφαλμάτων
        Map<String, String> errors = new LinkedHashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            // Καθαρισμός: Από "getByAmka.amka" κρατάμε ό,τι είναι μετά την τελευταία τελεία -> "amka"
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            String message = violation.getMessage();

            errors.merge(fieldName, message, (existing, newMsg) -> existing + " | " + newMsg);
        }

        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.validationError(
                        ErrorCode.VALIDATION_FAILED,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        errors,
                        request.getRequestURI()));
    }

    // Πιανει τα παντα που δεν εχουν πιασει οι προηγουμενοι
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllUncaughtException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error occurred: ", ex);

        String message = messageSource.getMessage(
                "error.internal.server",
                null,
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        message,
                        request.getRequestURI()
                ));
    }


    /**
     * Helper μέθοδος για να τραβάμε μεταφράσεις από το messages.properties.
     * Χρησιμοποιεί το LocaleContextHolder για να καταλάβει αυτόματα τη γλώσσα του χρήστη.
     */
    private String translate(String key, Object... args) {
        try {
            // Η getMessage με 4 παραμέτρους επιστρέφει το default αν δεν βρει το key
            return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            // Αν ξεχάσουμε να ορίσουμε ένα κλειδί στο properties,
            // επέστρεψε το ίδιο το κλειδί αντί να σκάσει η εφαρμογή.
            return key;
        }
    }


}