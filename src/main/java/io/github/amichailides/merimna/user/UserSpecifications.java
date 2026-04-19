package io.github.amichailides.merimna.user;

import io.github.amichailides.merimna.domain.Role;
import io.github.amichailides.merimna.domain.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    private UserSpecifications() {}

    public static Specification<User> searchByUsernameOrEmail(String term) {
        return (root, query, cb) -> {
            if (term == null || term.isBlank()) return null;

            String pattern = "%" + term.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("username")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
            );
        };
    }

    public static Specification<User> hasRole (Role role) {
        return (root, query, cb) ->
                role == null
                        ? null
                        : cb.equal(root.get("role"), role);
    }

    public static Specification<User> isActive(Boolean active) {
        return ((root, query, cb) ->
                active == null
                        ? null
                        : cb.equal(root.get("active"), active));
    }
}
