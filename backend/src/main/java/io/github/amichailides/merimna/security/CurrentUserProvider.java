package io.github.amichailides.merimna.security;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.Role;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final EmployeeRepository employeeRepository;

    public Employee getCurrentEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Authentication required");
        }

        if (!(authentication.getPrincipal() instanceof User currentUser)) {
            throw new InsufficientAuthenticationException("Invalid authentication principal");
        }

        return employeeRepository.findWithAccessDataById(currentUser.getEmployee().getId())
                .orElseThrow(() -> new AccessDeniedException("Employee not found for authenticated user"));
    }

    public Role getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Authentication required");
        }

        if (!(authentication.getPrincipal() instanceof User currentUser)) {
            throw new InsufficientAuthenticationException("Invalid authentication principal");
        }

        return currentUser.getRole();
    }
}