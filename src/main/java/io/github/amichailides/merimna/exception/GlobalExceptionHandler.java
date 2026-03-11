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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Μετατρέπει τα exceptions σε {@link ApiResponse} χρησιμοποιώντας
 * τους προκαθορισμένους κωδικούς του {@link ErrorCode}.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BaseDomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(
            BaseDomainException ex,
            HttpServletRequest request) {
        HttpStatus status = ex.getErrorCode().getStatus();

        // Παίρνουμε το localized message
        String detail = translate(ex.getErrorCode().getMessageKey(), ex.getArgs());

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(
                        ex.getErrorCode(),
                        status.value(),
                        status.getReasonPhrase(),
                        detail,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(BaseValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            BaseValidationException ex,
            HttpServletRequest request) {

        HttpStatus status = ex.getErrorCode().getStatus();

        String detail = translate(ex.getErrorCode().getMessageKey(), ex.getArgs());

        // Μετατροπή των error keys σε μηνύματα (i18n)
        Map<String, List<String>> localizedErrors = ex.getValidationErrors().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(this::translate)
                                .collect(Collectors.toList())
                ));

        return ResponseEntity
                .status(status)
                .body(ApiResponse.validationError(
                                ex.getErrorCode(),
                                status.value(),
                                status.getReasonPhrase(), // title
                                detail,
                                localizedErrors,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        HttpStatus status = ErrorCode.DATABASE_ERROR.getStatus();

        log.error("Database integrity violation: {}", ex.getMostSpecificCause().getMessage());

        String detail = translate(ErrorCode.DATABASE_ERROR.getMessageKey());

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(
                        ErrorCode.DATABASE_ERROR,
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        detail,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, List<String>> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldPath = error.getField();
            String message = translate(error.getDefaultMessage());
            errors.computeIfAbsent(fieldPath, k -> new ArrayList<>()).add(message);
        });

        ex.getBindingResult().getGlobalErrors().forEach(error -> {
            String message = translate(error.getDefaultMessage());
            errors.computeIfAbsent("_global", k -> new ArrayList<>()).add(message);
        });

        log.warn("Validation failed for {}: {}", request.getRequestURI(), errors);

        ErrorCode code = ErrorCode.VALIDATION_FAILED;

        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.validationError(
                        code,
                        code.getStatus().value(),
                        code.getStatus().getReasonPhrase(),
                        translate(code.getMessageKey()),
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
        String detail = translate(ErrorCode.INVALID_INPUT.getMessageKey());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ErrorCode.INVALID_INPUT,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        detail,
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

        String detail = String.format("Η τιμή '%s' δεν είναι έγκυρη για την παράμετρο '%s'.",
                providedValue, propertyName);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ErrorCode.INVALID_INPUT,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        detail,
                        request.getRequestURI()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        // Χρησιμοποιούμε LinkedHashMap για να κρατήσουμε τη σειρά των σφαλμάτων
        Map<String, List<String>> errors = new LinkedHashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            // Καθαρισμός: Από "getByAmka.amka" κρατάμε ό,τι είναι μετά την τελευταία τελεία -> "amka"
            String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            String message = violation.getMessage();

            errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(message);
        }

        String detail = translate(ErrorCode.VALIDATION_FAILED.getMessageKey());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.validationError(
                        ErrorCode.VALIDATION_FAILED,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        detail,
                        errors,
                        request.getRequestURI()));
    }

    // Πιάνει τα πάντα που δεν έχουν πιάσει οι προηγούμενοί
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllUncaughtException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error occurred: ", ex);

        String detail = translate(ErrorCode.INTERNAL_SERVER_ERROR.getMessageKey());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        detail,
                        request.getRequestURI()
                ));
    }


    /**
     * Helper μέθοδος για να τραβάμε μεταφράσεις από το messages.properties.
     * Χρησιμοποιεί το LocaleContextHolder για να καταλάβει αυτόματα τη γλώσσα του χρήστη.
     */
    private String translate(String key, Object... args) {
        return messageSource.getMessage(
                key,
                args,
                key, // fallback αν δεν υπάρχει το key
                LocaleContextHolder.getLocale()
        );
    }


}