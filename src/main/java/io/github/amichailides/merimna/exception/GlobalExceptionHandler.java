package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    // Constructor injection
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BeneficiaryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBeneficiaryNotFound(
            BeneficiaryNotFoundException ex,
            HttpServletRequest request) {

        // Παίρνουμε το localized message
        String message = messageSource.getMessage(
                ex.getMessageKey(),
                ex.getMessageArgs(),  //  Το ID σαν parameter
                LocaleContextHolder.getLocale()
        );

        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                message,
                request.getRequestURI(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        String message = messageSource.getMessage(
                "beneficiary.amkaAlreadyExists",
                null,
                LocaleContextHolder.getLocale()
        );


        ErrorResponse error= new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                message,
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // TODO: Future Refactoring - Αντικατάσταση του String message με Map<String, String>
    // για την ταυτόχρονη επιστροφή όλων των validation errors (field-level errors).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Παίρνουμε το πρώτο λάθος που θα βρει (π.χ. "Το επώνυμο είναι υποχρεωτικό")
        String errorMessage = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errorMessage,
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // TODO: Implement Global Type Mismatch Handler
// 1. Target Exception: MethodArgumentTypeMismatchException
// 2. Goal: Catch invalid Enum values (e.g. HouseUnit) or ID type errors in @PathVariables
// 3. Response: Return 400 Bad Request with a clear message:
//    "The value '{providedValue}' is not valid for the parameter '{parameterName}'"
// 4. Benefit: Provides a consistent, professional error response instead of Spring's default 500/400 trace.



}