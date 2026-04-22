package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.HouseUnit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long>,
        JpaSpecificationExecutor<Beneficiary> {

    boolean existsByAmka(String amka);

    boolean existsByAmkaAndPublicIdNot(String amka, String publicId);

    @EntityGraph(attributePaths = {
            "medications",
            "allergies",
            "legalRepresentatives"
    })
    Optional<Beneficiary> findWithDetailsByPublicId(String publicId);

    long countByHouseUnitAndIsActiveTrue(HouseUnit houseUnit);

    Optional<Beneficiary> findByPublicId(String publicId);

    boolean existsByPublicId(String publicId);

}
