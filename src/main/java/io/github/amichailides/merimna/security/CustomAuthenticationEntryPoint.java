package io.github.amichailides.merimna.security;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// TODO(#23): Refine authentication error mapping and add structured security logging
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JsonMapper jsonMapper;
    private final MessageSource messageSource;

    @Override
    public void commence(@NonNull HttpServletRequest request,
                         @NonNull HttpServletResponse response,
                         @NonNull AuthenticationException authException) throws IOException {

        ErrorCode errorCode = determineErrorCode(request, authException);
        String detail = resolveDetail(errorCode, request);
        ApiResponse<Void> apiError = buildErrorResponse(errorCode, detail, request);

        writeResponse(response, apiError, errorCode);
    }

    private ErrorCode determineErrorCode(HttpServletRequest request, AuthenticationException authException) {
        Object attr = request.getAttribute(SecurityConstants.AUTH_ERROR_CODE_ATTR);

        if (attr instanceof ErrorCode errorCode) {
            return errorCode;
        }

        return switch (authException) {
            case BadCredentialsException ignored -> ErrorCode.INVALID_CREDENTIALS;
            case DisabledException ignored -> ErrorCode.ACCOUNT_DISABLED;
            case LockedException ignored -> ErrorCode.ACCOUNT_LOCKED;
            case InsufficientAuthenticationException ignored -> ErrorCode.UNAUTHORIZED;
            default -> ErrorCode.AUTHENTICATION_FAILED;
        };
    }

    private String resolveDetail(ErrorCode errorCode, HttpServletRequest request) {
        return messageSource.getMessage(
                errorCode.getMessageKey(),
                null,
                "Authentication is required",
                request.getLocale()
        );
    }

    private ApiResponse<Void> buildErrorResponse(ErrorCode errorCode,
                                                 String detail,
                                                 HttpServletRequest request) {
        return ApiResponse.error(
                errorCode,
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                detail,
                request.getRequestURI()
        );
    }

    private void writeResponse(HttpServletResponse response,
                               ApiResponse<Void> apiError,
                               ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonMapper.writeValueAsString(apiError));
    }
}