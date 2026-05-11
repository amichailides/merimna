package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.HouseUnit;
import org.springframework.data.jpa.domain.Specification;

import java.text.Normalizer;
import java.util.Set;
import java.util.UUID;

/**
 * Fuzzy αναζήτηση σε firstName, lastName και amka (partial match).
 *
 * <p>Υποστηρίζει ελληνικούς χαρακτήρες με χρήση {@code unaccent} (DB-side)
 * και {@link #stripAccents} (Java-side) για ομοιόμορφη σύγκριση.</p>
 *
 * // TODO(#6): Replace LIKE-based globalSearch with pg_trgm-based indexing
 */
public class BeneficiarySpecifications {
    private BeneficiarySpecifications() {
    }

    public static Specification<Beneficiary> globalSearch(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isBlank()) {
                return cb.conjunction();
            }

            String cleanSearchTerm = stripAccents(searchTerm.toLowerCase());
            String pattern = "%" + cleanSearchTerm + "%";

            return cb.or(
                    cb.like(
                            cb.function("replace", String.class,
                                    cb.function("unaccent", String.class, cb.lower(root.get("firstName"))),
                                    cb.literal("ς"), cb.literal("σ")),
                            pattern),
                    cb.like(
                            cb.function("replace", String.class,
                                    cb.function("unaccent", String.class, cb.lower(root.get("lastName"))),
                                    cb.literal("ς"), cb.literal("σ")),
                            pattern),
                    cb.like(root.get("amka"), pattern)
            );
        };
    }

    public static Specification<Beneficiary> hasHouseUnitPublicId(UUID houseUnitPublicId) {
        return (root, query, cb) ->
                houseUnitPublicId == null
                        ? cb.conjunction()
                        : cb.equal(root.join("houseUnit").get("publicId"), houseUnitPublicId);
    }

    public static Specification<Beneficiary> hasAmka(String amka) {
        return (root, query, cb) ->
                (amka == null || amka.isBlank())
                        ? cb.conjunction()
                        : cb.equal(root.get("amka"), amka);
    }

    public static Specification<Beneficiary> inHouseUnits(Set<HouseUnit> houseUnits) {
        return (root, query, cb) -> {
            if (houseUnits == null || houseUnits.isEmpty()) {
                return cb.disjunction(); // always false
            }

            return root.get("houseUnit").in(houseUnits);
        };
    }

    public static Specification<Beneficiary> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    /**
     * Αφαιρεί τους τόνους και τα διακριτικά σημάδια.
     * 1. Το NFD "ξεκολλάει" τους τόνους από τα γράμματα (π.χ. το 'ά' γίνεται 'α' + '´').
     * 2. Το Regex \\p{M} εντοπίζει και διαγράφει αυτά τα "ξεκολλημένα" σημάδια.
     */
    private static String stripAccents(String s) {
        if (s == null) return null;
        String normalized = Normalizer.normalize(s.toLowerCase(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "")
                .replace('ς', 'σ');
    }
}