package io.github.amichailides.merimna.medication.audit;

import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.domain.Medication;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class MedicationChangeDetector {

    public EntityChangeSet detectChanges(Medication medication, MedicationUpdateDTO dto) {
        return EntityChangeSet.builder()
                .trackIfPresent("name", medication.getName(), dto.name())
                .trackIfPresent("dosage", medication.getDosage(), dto.dosage())
                .trackIfPresent("frequency", medication.getFrequency(), dto.frequency())
                .trackIfPresent("instructions", medication.getInstructions(), dto.instructions())
                .trackIfPresent(
                        "administrationTimes",
                        medication.getAdministrationTimes(),
                        dto.administrationTimes())
                .build();
    }
}
