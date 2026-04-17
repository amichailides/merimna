package io.github.amichailides.merimna.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.amichailides.merimna.common.error.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Ενιαία δομή απόκρισης για όλα τα REST endpoints.
 *
 * <p>Καλύπτει τόσο επιτυχημένες αποκρίσεις (με {@code data})
 * όσο και σφάλματα (με {@code title}, {@code detail}, {@code validationErrors}).</p>
 *
 * @param <T> ο τύπος του {@code data} payload
 * @see ErrorCode
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // ← Δεν εμφανίζει null fields
@JsonPropertyOrder({
        "type",
        "title",
        "status",
        "detail",
        "validationErrors",
        "data",
        "path",
        "timestamp"
})
public class ApiResponse<T> {
    private ErrorCode type;
    private T data;
    private int status;
    private String title;       // null σε success
    private Map<String, List<String>> validationErrors;  // null σε non-validation errors, το @JsonInclude το κρύβει
    private String detail;     // null σε success
    private Map<String, Object> metadata;
    private String path;        // null σε success
    private String timestamp;


    public static <T> ApiResponse<T> error(ErrorCode errorCode, int status, String error,
                                           String message, String path) {
        // TODO(#22): Add structured error context (e.g. entity ids) to error responses instead of embedding in messages
        return ApiResponse.<T>builder()
                .type(errorCode)
                .status(status)
                .title(error)
                .detail(message)
                .path(path)
                .timestamp(formatTimestamp())
                .build();
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, int status, String error,
                                           String message, String path,
                                           Map<String, Object> metadata) {
        return ApiResponse.<T>builder()
                .type(errorCode)
                .status(status)
                .title(error)
                .detail(message)
                .metadata(metadata)
                .path(path)
                .timestamp(formatTimestamp())
                .build();
    }

    public static <T> ApiResponse<T> validationError(ErrorCode errorCode, int status,
                                                     String error, String detail, Map<String, List<String>> errors,
                                                     String path) {
        // TODO(#22): Add structured error context (e.g. entity ids) to error responses instead of embedding in messages
        return ApiResponse.<T>builder()
                .type(errorCode)
                .status(status)
                .title(error)
                .detail(detail)
                .validationErrors(errors)
                .path(path)
                .timestamp(formatTimestamp())
                .build();
    }


    private static String formatTimestamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
