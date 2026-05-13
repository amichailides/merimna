package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.domain.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    Optional<Allergy> findByPublicId(UUID publicId);

    List<Allergy> findAllByBeneficiaryPublicId(UUID beneficiaryPublicId);

    boolean existsByBeneficiaryIdAndSubstanceIgnoreCaseAndIdNot(
            Long beneficiaryId,
            String substance,
            Long id
    );

    boolean existsByBeneficiaryIdAndSubstanceIgnoreCase(Long beneficiaryId, String substance);
}
