package io.github.amichailides.merimna.specification;

import io.github.amichailides.merimna.model.Beneficiary;
import org.springframework.data.jpa.domain.Specification;
import java.text.Normalizer;

public class BeneficiarySpecifications {
    private BeneficiarySpecifications() {}

    public static Specification<Beneficiary> globalSearch(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isEmpty()) return null;

            // Καθαρίζουμε τους τόνους από το search term (Java side)
            String cleanSearchTerm = stripAccents(searchTerm.toLowerCase());
            String pattern = "%" + cleanSearchTerm + "%";

            // Χρησιμοποιούμε την unaccent της Postgres (DB side)
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