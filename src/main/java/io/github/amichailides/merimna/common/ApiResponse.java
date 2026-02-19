package io.github.amichailides.merimna.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // ← Δεν εμφανίζει null fields
public class ApiResponse<T> {
    private boolean success;
    private ErrorCode errorCode;
    private T data;
    private int status;
    private String error;       // null σε success
    private String message;     // null σε success
    private String path;        // null σε success
    private String timestamp;

    // factory για success
    public static <T> ApiResponse<T> success(T data, String message, int status) {
        // Type witness: βοηθάμε τον compiler να συμπεράνει το T σε generic builder chain
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .status(status)
                .timestamp(formatTimestamp())
                .build();
    }

    //factory για error
    public static <T> ApiResponse<T> error(ErrorCode errorCode, int status, String error,
                                           String message, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .errorCode(errorCode)
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .timestamp(formatTimestamp())
                .build();
    }


    // Helper για timestamp formatting
    private static String formatTimestamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

}
