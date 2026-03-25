package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.domain.Beneficiary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepositoryImplementation<Beneficiary, Long>,
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

}
