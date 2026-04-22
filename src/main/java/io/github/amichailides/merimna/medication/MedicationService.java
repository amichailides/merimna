package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;

import java.util.List;

public interface MedicationService {
    MedicationReadOnlyDTO addMedication (String beneficiaryPublicId, MedicationCreateDTO dto);

    MedicationReadOnlyDTO updateMedication(String beneficiaryPublicId, Long medicationId, MedicationUpdateDTO dto);

    void deleteMedication (String beneficiaryPublicId, Long medicationId);

    MedicationReadOnlyDTO getMedication(String beneficiaryPublicId, Long medicationId);

    List<MedicationReadOnlyDTO> getMedicationsByBeneficiary(String beneficiaryPublicId);
}
