package io.github.amichailides.merimna.security;

import io.github.amichailides.merimna.security.dto.AuthResponse;
import io.github.amichailides.merimna.security.dto.LoginRequest;
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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );


            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            return new AuthResponse(token);

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
}
