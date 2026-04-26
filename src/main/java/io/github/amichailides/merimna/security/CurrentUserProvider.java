package io.github.amichailides.merimna.security;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.User;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
            throw new AccessDeniedException(ErrorCode.UNAUTHORIZED.name());
        }

        if (!(authentication.getPrincipal() instanceof User currentUser)) {
            throw new AccessDeniedException(ErrorCode.UNAUTHORIZED.name());
        }

        return employeeRepository.findWithAccessDataById(currentUser.getEmployee().getId())
                .orElseThrow(() -> new AccessDeniedException("Employee not found"));
    }
}
