package io.github.amichailides.merimna.exception;

import io.github.amichailides.merimna.common.response.ApiResponse;
import io.github.amichailides.merimna.common.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Converts application exceptions into standardized {@link ApiResponse} error responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BaseApplicationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(
            BaseApplicationException ex,
            HttpServletRequest request) {
        HttpStatus status = ex.getErrorCode().getStatus();

        String detail = translate(ex.getErrorCode().getMessageKey(), ex.getArgs());

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(
                        ex.getErrorCode(),
                        status.value(),
                        status.getReasonPhrase(),
                        detail,
                        request.getRequestURI(),
                        ex.getContext().isEmpty() ? null : ex.getContext()
                ));
    }

    @ExceptionHandler(BaseValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            BaseValidationException ex,
            HttpServletRequest request) {

        HttpStatus status = ex.getErrorCode().getStatus();

        String detail = translate(ex.getErrorCode().getMessageKey(), ex.getArgs());

        // Localize validation error keys before returning them in the API response.
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

        log.error("Database integrity violation while processing {}: {}",
                request.getRequestURI(),
                ex.getMostSpecificCause().getMessage());

        String detail = translate(ErrorCode.DATABASE_ERROR.getMessageKey());

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(
                        ErrorCode.DATABASE_ERROR,
                        status.value(),
                        status.getReasonPhrase(),
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

        log.debug("Validation failed for {}: {}", request.getRequestURI(), errors);

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

    // TODO: [Refactor] Improve this handler by inspecting Jackson's InvalidFormatException.
    // Goal: extract the exact field name and rejected value from the request body,
    // so the response can say which field is invalid and what value was provided.
    // Useful APIs: formatException.getPath(), formatException.getValue().
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn("Invalid request body for {}: {}",
                request.getRequestURI(),
                ex.getMostSpecificCause().getMessage());
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String propertyName = ex.getName();
        Object providedValue = ex.getValue();
        Class<?> requiredType = ex.getRequiredType();

        log.warn("Type mismatch for parameter '{}': {}", propertyName, providedValue);

        String detail;

        if (UUID.class.equals(requiredType)) {
            detail = messageSource.getMessage(
                    "error.request.parameter.invalidUuid",
                    null,
                    LocaleContextHolder.getLocale()
            );
        } else if (requiredType != null && requiredType.isEnum()) {
            detail = messageSource.getMessage(
                    "error.request.parameter.typeMismatchEnum",
                    null,
                    LocaleContextHolder.getLocale()
            );
        } else {
            detail = messageSource.getMessage(
                    "error.request.parameter.typeMismatch",
                    null,
                    LocaleContextHolder.getLocale()
            );
        }

        Map<String, Object> context = new LinkedHashMap<>();
        context.put("parameter", propertyName);
        context.put("providedValue", String.valueOf(providedValue));

        if (requiredType != null) {
            context.put("expectedType", requiredType.getSimpleName());

            if (requiredType.isEnum()) {
                context.put("validValues", Arrays.stream(requiredType.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")));
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ErrorCode.INVALID_INPUT,
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        detail,
                        request.getRequestURI(),
                        context
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        // Preserve validation error order in the API response.
        Map<String, List<String>> errors = new LinkedHashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();

            // Keep only the actual parameter/field name from paths like "getByAmka.amka".
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

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFound(
            NoResourceFoundException ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;

        log.warn("No resource found for request path: {}", request.getRequestURI());

        String detail = messageSource.getMessage(
                errorCode.getMessageKey(),
                null,
                "Δεν βρέθηκε διαθέσιμος πόρος για τη συγκεκριμένη διεύθυνση.",
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(
                        errorCode,
                        errorCode.getStatus().value(),
                        errorCode.getStatus().getReasonPhrase(),
                        detail,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllUncaughtException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error while processing {}", request.getRequestURI(), ex);

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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        log.warn("Access denied for request {}", request.getRequestURI());
        ErrorCode errorCode = ErrorCode.FORBIDDEN;

        String detail = translate(errorCode.getMessageKey());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(
                        errorCode,
                        errorCode.getStatus().value(),
                        errorCode.getStatus().getReasonPhrase(),
                        detail,
                        request.getRequestURI()
                ));
    }


    private String translate(String key, Object... args) {
        return messageSource.getMessage(
                key,
                args,
                key, // fallback αν δεν υπάρχει το key
                LocaleContextHolder.getLocale()
        );
    }
}