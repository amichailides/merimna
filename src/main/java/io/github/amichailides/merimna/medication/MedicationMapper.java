package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;
import io.github.amichailides.merimna.domain.Medication;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class MedicationMapper {

    public Medication toEntity(MedicationCreateDTO dto) {
        if (dto == null) return null;

        return Medication.builder()
                .name(dto.name())
                .dosage(dto.dosage())
                .frequency(dto.frequency())
                .administrationTimes(dto.administrationTimes())
                .instructions(dto.instructions())
                .build();
    }

    public MedicationReadOnlyDTO toDTO(Medication entity) {
        if (entity == null) return null;

        return new MedicationReadOnlyDTO(
                entity.getId(),
                entity.getName(),
                entity.getDosage(),
                entity.getFrequency(),
                entity.getAdministrationTimes(),
                entity.getInstructions()
        );
    }

    public void updateEntity(Medication existing, MedicationUpdateDTO dto) {

        updateIfNotNull(dto.name(), existing::setName);
        updateIfNotNull(dto.dosage(), existing::setDosage);
        updateIfNotNull(dto.frequency(), existing::setFrequency);
        updateIfNotNull(dto.administrationTimes(), existing::setAdministrationTimes);
        // Allow clearing instructions (empty value is valid if explicitly provided)
        if (dto.instructions() != null) existing.setInstructions(dto.instructions());
    }

    private void updateIfNotNull (String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }

    }
}