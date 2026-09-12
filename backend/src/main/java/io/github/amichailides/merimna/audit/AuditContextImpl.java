package io.github.amichailides.merimna.audit;

import io.github.amichailides.merimna.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuditContextImpl implements AuditContext {

    private final HttpServletRequest request;

    @Override
    public UUID getUserPublicId() {
        return getCurrentUser() != null
                ? getCurrentUser().getPublicId()
                : null;
    }

    @Override
    public UUID getEmployeePublicId() {
        User user = getCurrentUser();

        return user != null && user.getEmployee() != null
                ? user.getEmployee().getPublicId()
                : null;
    }

    @Override
    public String getIpAddress() {
        return request.getRemoteAddr();
    }

    @Override
    public String getUserAgent() {
        return request.getHeader("User-Agent");
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User user)) {
            return null;
        }

        return user;
    }
}