package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationDiscontinueDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface MedicationService {
    MedicationReadOnlyDTO addMedication(UUID beneficiaryPublicId, MedicationCreateDTO dto);

    MedicationReadOnlyDTO updateMedication(UUID beneficiaryPublicId, UUID medicationPublicId, MedicationUpdateDTO dto);

    MedicationReadOnlyDTO getMedicationByPublicId(UUID beneficiaryPublicId, UUID medicationPublicId);

    List<MedicationReadOnlyDTO> getMedicationsByBeneficiary(UUID beneficiaryPublicId, boolean includeInactive);

    MedicationReadOnlyDTO discontinueMedication(
            UUID beneficiaryPublicId,
            UUID medicationPublicId,
            MedicationDiscontinueDTO dto
    );
}
