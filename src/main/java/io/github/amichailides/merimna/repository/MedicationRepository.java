package io.github.amichailides.merimna.repository;

import io.github.amichailides.merimna.model.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Page<Medication> findAllByBeneficiaryId(Long beneficiaryId, Pageable pageable);

}
