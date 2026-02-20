package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ApiResponse;
import io.github.amichailides.merimna.common.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        String message = messageSource.getMessage(
                ex.getMessageKey(),
                ex.getMessageArgs(),  //  Το ID σαν parameter
                LocaleContextHolder.getLocale()
        );

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

        // 2. Μεταφράζουμε κάθε σφάλμα μέσα στο Map
        Map<String, String> localizedErrors = ex.getValidationErrors().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> translate(entry.getValue()), // Μετάφραση της τιμής (value)
                        (existing, replacement) -> existing,    // Merge function (σε περίπτωση διπλότυπων)
                        LinkedHashMap::new                                  // Διατήρηση της σειράς (Insertion Order)
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
    public ResponseEntity<ApiResponse<Void>> handleConflict(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        String message = messageSource.getMessage(
                "beneficiary.amkaAlreadyExists",
                null,
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        ErrorCode.AMKA_ALREADY_EXISTS,
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        message,
                        request.getRequestURI()
                ));
    }

    // TODO: Future Refactoring - Αντικατάσταση του String message με Map<String, String>
    // για την ταυτόχρονη επιστροφή όλων των validation errors (field-level errors).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String errorMessage;
        var bindingResult = ex.getBindingResult();

        // 1. Ελέγχουμε αν υπάρχει Field Error (π.χ. @NotBlank στο firstName)
        if (bindingResult.hasFieldErrors()) {
            var fieldError = bindingResult.getFieldError();
            errorMessage = fieldError.getField() + ": " + fieldError.getDefaultMessage();
        }
        // 2. Ελέγχουμε αν υπάρχει Global Error (π.χ. ο @AtLeastOnePhonePresent)
        else if (bindingResult.hasGlobalErrors()) {
            var globalError = bindingResult.getGlobalError();
            errorMessage = globalError.getDefaultMessage(); // Εδώ παίρνει το "{emergency.contact.missing}"
        } else {
            errorMessage = "Validation error";
        }


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ErrorCode.VALIDATION_FAILED,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        errorMessage,
                        request.getRequestURI()
                ));
    }

    /**
     * Helper μέθοδος για να τραβάμε μεταφράσεις από το messages.properties.
     * Χρησιμοποιεί το LocaleContextHolder για να καταλάβει αυτόματα τη γλώσσα του χρήστη.
     */
    private String translate(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            // Αν ξεχάσουμε να ορίσουμε ένα κλειδί στο properties,
            // επέστρεψε το ίδιο το κλειδί αντί να σκάσει η εφαρμογή.
            return key;
        }
    }

    // TODO: Implement Global Type Mismatch Handler
// 1. Target Exception: MethodArgumentTypeMismatchException
// 2. Goal: Catch invalid Enum values (e.g. HouseUnit) or ID type errors in @PathVariables
// 3. Response: Return 400 Bad Request with a clear message:
//    "The value '{providedValue}' is not valid for the parameter '{parameterName}'"
// 4. Benefit: Provides a consistent, professional error response instead of Spring's default 500/400 trace.


}