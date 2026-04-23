package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface MedicationService {
    MedicationReadOnlyDTO addMedication (UUID beneficiaryPublicId, MedicationCreateDTO dto);

    MedicationReadOnlyDTO updateMedication(UUID beneficiaryPublicId, Long medicationId, MedicationUpdateDTO dto);

    void deleteMedication (UUID beneficiaryPublicId, Long medicationId);

    MedicationReadOnlyDTO getMedication(UUID beneficiaryPublicId, Long medicationId);

    List<MedicationReadOnlyDTO> getMedicationsByBeneficiary(UUID beneficiaryPublicId);
}
