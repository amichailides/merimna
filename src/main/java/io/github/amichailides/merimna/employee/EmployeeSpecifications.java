package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.domain.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.text.Normalizer;
import java.util.UUID;

public class EmployeeSpecifications {
    private EmployeeSpecifications() {
    }

    public static Specification<Employee> globalSearch(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isBlank()) {
                return cb.conjunction();
            }

            String pattern = "%" + stripAccents(searchTerm) + "%";

            return cb.or(
                    searchLike(cb, cb.function("unaccent", String.class, cb.lower(root.get("firstName"))), pattern),
                    searchLike(cb, cb.function("unaccent", String.class, cb.lower(root.get("lastName"))), pattern),
                    cb.like(cb.lower(cb.function("SPLIT_PART", String.class,
                            root.get("contactEmail"), cb.literal("@"), cb.literal(1))), pattern),
                    cb.like(root.get("mobileNumber"), pattern)
            );
        };
    }

    public static Specification<Employee> hasPositionCode(String positionCode) {
        return (root, query, cb) ->
                positionCode == null || positionCode.isBlank()
                        ? cb.conjunction()
                        : cb.equal(root.get("position").get("code"), positionCode);
    }

    public static Specification<Employee> belongsToHouseUnitPublicId(UUID houseUnitPublicId) {
        return (root, query, cb) -> {
            if (houseUnitPublicId == null) {
                return cb.conjunction();
            }

            query.distinct(true);

            return cb.equal(
                    root.joinSet("assignments")
                            .join("houseUnit")
                            .get("publicId"),
                    houseUnitPublicId
            );
        };
    }

    public static Specification<Employee> isActive(Boolean active) {
        return (root, query, cb) ->
                active == null
                        ? cb.conjunction()
                        : cb.equal(root.get("isActive"), active);
    }

    private static String stripAccents(String s) {
        if (s == null) {
            return null;
        }

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