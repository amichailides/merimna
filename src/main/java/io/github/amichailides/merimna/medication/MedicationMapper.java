package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.domain.Medication;
import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;
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
                .startedAt(dto.startedAt())
                .build();
    }

    public MedicationReadOnlyDTO toDTO(Medication entity) {
        if (entity == null) return null;

        return new MedicationReadOnlyDTO(
                entity.getPublicId(),
                entity.getName(),
                entity.getDosage(),
                entity.getFrequency(),
                entity.getAdministrationTimes(),
                entity.getInstructions(),
                entity.getStartedAt(),
                entity.getEndedAt(),
                entity.isActive()
        );
    }

    public void updateEntity(Medication existing, MedicationUpdateDTO dto) {
        Objects.requireNonNull(existing, "existing medication must not be null");
        Objects.requireNonNull(dto, "medication update dto must not be null");

        updateIfNotBlank(dto.name(), existing::setName);
        updateIfNotBlank(dto.dosage(), existing::setDosage);
        updateIfNotBlank(dto.frequency(), existing::setFrequency);
        updateIfNotBlank(dto.administrationTimes(), existing::setAdministrationTimes);
        updateIfNotBlank(dto.instructions(), existing::setInstructions);

        if (dto.startedAt() != null) {
            existing.changeStartedAt(dto.startedAt());
        }
    }

    private void updateIfNotBlank(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }
}
