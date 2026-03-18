package io.github.amichailides.merimna.repository;

import io.github.amichailides.merimna.model.LegalRepresentative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LegalRepresentativeRepository extends JpaRepository<LegalRepresentative, Long> {

    Optional<LegalRepresentative> findByIdAndBeneficiariesId(Long legalRepresentativeId, Long beneficiaryId);
}
