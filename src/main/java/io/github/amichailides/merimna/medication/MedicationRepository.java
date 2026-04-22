package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.domain.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Page<Medication> findAllByBeneficiaryId(Long beneficiaryId, Pageable pageable);

    Optional<Medication> findByIdAndBeneficiaryPublicId(Long medicationId, String beneficiaryPublicId);

}
