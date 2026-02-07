package io.github.amichailides.merimna.specification;

import io.github.amichailides.merimna.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    private UserSpecifications() {};

    public static Specification<User> globalSearch(String term) {
        return (root, query, cb) -> {
            if (term == null || term.isBlank()) return null;

            String pattern = "%" + term.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), pattern),
                    cb.like(cb.lower(root.get("lastName")), pattern),
                    cb.like(root.get("mobile"), pattern)
            );
        };
    }
}
