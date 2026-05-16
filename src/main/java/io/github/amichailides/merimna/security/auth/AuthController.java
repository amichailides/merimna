package io.github.amichailides.merimna.security.auth;

import io.github.amichailides.merimna.security.auth.dto.AuthResponse;
import io.github.amichailides.merimna.security.auth.dto.LoginRequest;
import io.github.amichailides.merimna.security.auth.dto.RefreshRequest;
import io.github.amichailides.merimna.security.config.SecurityProperties;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SecurityProperties securityProperties;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Validated(ValidationGroupSequence.class) @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        String userAgent = httpRequest.getHeader("User-Agent");
        String ipAddress = httpRequest.getRemoteAddr();

        AuthResponse authResponse = authService.login(request, userAgent, ipAddress);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", authResponse.refreshToken())
                .httpOnly(true)
                .secure(securityProperties.getRefreshToken().isSecureCookie())
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(securityProperties.getRefreshToken().getExpiration())
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody(required = false) RefreshRequest refreshRequest,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        String refreshToken = extractRefreshToken(
                httpRequest,
                refreshRequest != null ? refreshRequest.refreshToken() : null
        );

        String userAgent = httpRequest.getHeader("User-Agent");
        String ipAddress = httpRequest.getRemoteAddr();

        AuthResponse authResponse = authService.refresh(refreshToken, userAgent, ipAddress);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", authResponse.refreshToken())
                .httpOnly(true)
                .secure(securityProperties.getRefreshToken().isSecureCookie())
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(securityProperties.getRefreshToken().getExpiration())
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody(required = false) RefreshRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        String refreshToken = extractRefreshToken(httpRequest,
                request != null ? request.refreshToken() : null);

        authService.logout(refreshToken);
        clearRefreshTokenCookie(httpResponse);

        return ResponseEntity.noContent().build();
    }

    private String extractRefreshToken(HttpServletRequest httpRequest, String bodyRefreshToken) {
        if (httpRequest.getCookies() != null) {
            return Arrays.stream(httpRequest.getCookies())
                    .filter(cookie -> "refresh_token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(bodyRefreshToken);
        }

        return bodyRefreshToken;
    }

    private void clearRefreshTokenCookie(HttpServletResponse httpResponse) {
        ResponseCookie clearedCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(securityProperties.getRefreshToken().isSecureCookie())
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(Duration.ZERO)
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, clearedCookie.toString());
    }
}
