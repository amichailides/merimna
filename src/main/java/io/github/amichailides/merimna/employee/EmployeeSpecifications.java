package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeePosition;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.text.Normalizer;

public class EmployeeSpecifications {
    private EmployeeSpecifications() {
    }

    public static Specification<Employee> globalSearch(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isBlank()) return null;

            String pattern = "%" + stripAccents(searchTerm) + "%";

            return cb.or(
                    searchLike(cb, cb.function("unaccent", String.class, cb.lower(root.get("firstName"))), pattern),
                    searchLike(cb, cb.function("unaccent", String.class, cb.lower(root.get("lastName"))), pattern),
                    cb.like(cb.lower(root.get("email")), pattern),
                    cb.like(root.get("mobileNumber"), pattern)
            );
        };
    }

    public static Specification<Employee> hasPosition(EmployeePosition position) {
        return (root, query, cb) ->
                position == null
                        ? null
                        : cb.equal(root.get("position"), position);
    }

    public static Specification<Employee> belongsToHouseUnit(String houseUnitCode) {
        return (root, query, cb) -> {
            if (houseUnitCode == null || houseUnitCode.isBlank()) {
                return null;
            }
            query.distinct(true);

            return cb.equal(
                    root.joinSet("assignments")
                            .join("houseUnit")
                            .get("code"),
                    houseUnitCode
            );
        };
    }

    public static Specification<Employee> isActive(Boolean active) {
        return (root, query, cb) ->
                active == null ? null : cb.equal(root.get("isActive"), active);
    }




    private static String stripAccents (String s){
        if (s == null) return null;
        String normalized = Normalizer.normalize(s.toLowerCase(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "")
                .replace('ς', 'σ');
    }

    private static Predicate searchLike(CriteriaBuilder cb, Expression<String> field, String pattern) {
        return cb.like(
                cb.function("replace", String.class, field, cb.literal("ς"), cb.literal("σ")),
                pattern
        );
    }
}