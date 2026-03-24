package io.github.amichailides.merimna.repository;

import io.github.amichailides.merimna.domain.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    Optional<Allergy> findByIdAndBeneficiaryId(Long allergyId, Long beneficiaryId);
    List<Allergy> findAllByBeneficiaryId(Long beneficiaryId);
}
