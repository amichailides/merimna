package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.ApiResponse;
import io.github.amichailides.merimna.common.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

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
        }
        else {
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



    // TODO: Implement Global Type Mismatch Handler
// 1. Target Exception: MethodArgumentTypeMismatchException
// 2. Goal: Catch invalid Enum values (e.g. HouseUnit) or ID type errors in @PathVariables
// 3. Response: Return 400 Bad Request with a clear message:
//    "The value '{providedValue}' is not valid for the parameter '{parameterName}'"
// 4. Benefit: Provides a consistent, professional error response instead of Spring's default 500/400 trace.



}