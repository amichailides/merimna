package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;

import java.util.List;

public interface MedicationService {
    MedicationReadOnlyDTO addMedication(Long beneficiaryId, MedicationCreateDTO dto);

    MedicationReadOnlyDTO updateMedication(Long beneficiaryId, Long medicationId, MedicationUpdateDTO dto);

    void deleteMedication(Long beneficiaryId, Long medicationId);

    List<MedicationReadOnlyDTO> getMedicationsByBeneficiary(Long beneficiaryId);

    MedicationReadOnlyDTO getMedication(Long beneficiaryId, Long medicationId);
}
