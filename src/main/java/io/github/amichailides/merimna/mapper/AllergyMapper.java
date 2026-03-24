package io.github.amichailides.merimna.mapper;

import io.github.amichailides.merimna.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.domain.Allergy;
import org.springframework.stereotype.Component;

@Component
public class AllergyMapper {

    /**
     * Μετατρέπει το DTO σε Entity για αποθήκευση.
     * Το ID και ο Beneficiary δεν ορίζονται εδώ, καθώς το ID παράγεται από τη βάση
     * και ο Beneficiary συγχρονίζεται μέσω της helper μεθόδου στην κλάση Beneficiary.
     */
    public Allergy toEntity(AllergyCreateDTO dto) {
        if (dto == null) return null;

        return Allergy.builder()
                .substance(dto.substance())
                .severity(dto.severity())
                .reaction(dto.reaction())
                .build();
    }

    public AllergyReadOnlyDTO toDTO(Allergy entity) {
        if (entity == null) return null;

        return new AllergyReadOnlyDTO(
                entity.getId(),
                entity.getSubstance(),
                entity.getSeverity(),
                entity.getReaction()
        );
    }

    public void updateEntity(AllergyUpdateDTO dto, Allergy existing) {
        existing.setSubstance(dto.substance());
        existing.setSeverity(dto.severity());
        existing.setReaction(dto.reaction());
    }


}