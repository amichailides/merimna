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

    Optional<Beneficiary> findByAmka(String amka);

    boolean existsByAmka(String amka);

    boolean existsByAmkaAndIdNot(String amka, Long id);

    @EntityGraph(attributePaths = {
            "medications",
            "allergies",
            "legalRepresentatives"
    })
    Optional<Beneficiary> findWithDetailsById(Long id);

    long countByHouseUnitAndIsActiveTrue(HouseUnit houseUnit);

}
