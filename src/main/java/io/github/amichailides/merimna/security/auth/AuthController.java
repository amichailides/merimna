package io.github.amichailides.merimna.security.auth;

import io.github.amichailides.merimna.security.auth.dto.AuthResponse;
import io.github.amichailides.merimna.security.auth.dto.LoginRequest;
import io.github.amichailides.merimna.security.auth.dto.RefreshRequest;
import io.github.amichailides.merimna.security.config.SecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SecurityProperties securityProperties;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        String userAgent = httpRequest.getHeader("User-Agent");
        String ipAddress = httpRequest.getRemoteAddr();

        AuthResponse authResponse = authService.login(request, userAgent, ipAddress);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", authResponse.refreshToken())
                .httpOnly(true)
                .secure(securityProperties.getRefreshToken().isSecureCookie())
                .sameSite("Strict")
                .path("/auth")
                .maxAge(securityProperties.getRefreshToken().getExpiration())
                .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshRequest request) {

        return ResponseEntity.ok(authService.refresh(request.refreshToken()));
    }
}
