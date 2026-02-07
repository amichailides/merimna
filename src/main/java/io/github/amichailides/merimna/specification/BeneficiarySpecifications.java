package io.github.amichailides.merimna.specification;

import io.github.amichailides.merimna.model.Beneficiary;
import org.springframework.data.jpa.domain.Specification;

public class BeneficiarySpecifications {
    private BeneficiarySpecifications() {}

    public static Specification<Beneficiary> globalSearch(String searchTerm) {
        return (root, query, cb) ->{
            if (searchTerm == null || searchTerm.isEmpty()) return null;
            String pattern = "%" + searchTerm.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), pattern),
            cb.like(cb.lower(root.get("lastName")), pattern),
            cb.like(root.get("amka"), pattern)
                    );
        };

    }

}
