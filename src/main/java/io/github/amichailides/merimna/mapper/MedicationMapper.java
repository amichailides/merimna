package io.github.amichailides.merimna.mapper;

import io.github.amichailides.merimna.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.model.Medication;
import org.springframework.stereotype.Component;

@Component
public class MedicationMapper {

    /**
     * Μετατρέπει το DTO σε Entity (Embeddable).
     */
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

    /**
     * Μετατρέπει την Entity σε DTO για το Front-end.
     */
    public MedicationReadOnlyDTO toDTO(Medication entity) {
        if (entity == null) return null;

        return new MedicationReadOnlyDTO(
                entity.getName(),
                entity.getDosage(),
                entity.getFrequency(),
                entity.getAdministrationTimes(),
                entity.getInstructions()
        );
    }
}