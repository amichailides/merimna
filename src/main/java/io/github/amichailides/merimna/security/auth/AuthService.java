package io.github.amichailides.merimna.security.auth;

import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.security.jwt.JwtService;
import io.github.amichailides.merimna.security.auth.dto.AuthResponse;
import io.github.amichailides.merimna.security.auth.dto.LoginRequest;
import io.github.amichailides.merimna.security.exception.AccountDisabledException;
import io.github.amichailides.merimna.security.exception.AccountLockedException;
import io.github.amichailides.merimna.security.exception.AuthenticationFailedException;
import io.github.amichailides.merimna.security.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
// TODO #28: Record audit event for login success/failure and logout.
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse login(LoginRequest request, String userAgent, String ipAddress) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            User user = (User) Objects.requireNonNull(authentication.getPrincipal());

            String accessToken  = jwtService.generateToken(user);
            String refreshToken = refreshTokenService.createRefreshToken(user, userAgent, ipAddress);

            return new AuthResponse(accessToken, refreshToken);

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            throw new InvalidCredentialsException();
        } catch (LockedException ex) {
            throw new AccountLockedException();
        } catch (DisabledException ex) {
            throw new AccountDisabledException();
        } catch (AuthenticationException ex) {
            throw new AuthenticationFailedException();
        }
    }

    public AuthResponse refresh(String rawRefreshToken) {
        User user = refreshTokenService.validateAndGetUser(rawRefreshToken);
        String accessToken = jwtService.generateToken(user);

        // TODO #29: Rotate refresh token on refresh and detect reuse of replaced tokens.
        return new AuthResponse(accessToken, rawRefreshToken);
    }

    @Transactional
    public void logout(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }
        refreshTokenService.revokeToken(rawToken);
    }
}
