package io.github.amichailides.merimna.mapper;

import io.github.amichailides.merimna.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.dto.MedicationUpdateDTO;
import io.github.amichailides.merimna.model.Medication;
import org.springframework.stereotype.Component;

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

    // TODO implement helper method updateIfNotNull()
    // updateIfNotNull(dto.name(), entity::setName);
    public void updateEntity(Medication existing, MedicationUpdateDTO dto) {

        if (dto.name() != null && !dto.name().isBlank() ) existing.setName(dto.name());
        if (dto.dosage() != null && !dto.dosage().isBlank()) existing.setDosage(dto.dosage());
        if (dto.frequency() != null && !dto.frequency().isBlank()) existing.setFrequency(dto.frequency());
        if (dto.administrationTimes() != null && !dto.administrationTimes().isBlank()) existing.setAdministrationTimes(dto.administrationTimes());
        // Αφήνουμε blank στο instructions (αν Θέλει να τη σβήσει)
        if (dto.instructions() != null) existing.setInstructions(dto.instructions());


    }
}