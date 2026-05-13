package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.domain.Allergy;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;

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
                .substance(normalize(dto.substance()))
                .severity(dto.severity())
                .reaction(dto.reaction())
                .build();
    }

    public AllergyReadOnlyDTO toDTO(Allergy entity) {
        if (entity == null) return null;

        return new AllergyReadOnlyDTO(
                entity.getPublicId(),
                entity.getSubstance(),
                entity.getSeverity(),
                entity.getReaction()
        );
    }

    public void updateEntity(Allergy existing, AllergyUpdateDTO dto) {
        Objects.requireNonNull(existing, "existing allergy must not be null");
        Objects.requireNonNull(dto, "allergy update dto must not be null");

        updateIfNotBlank(dto.substance(), value -> existing.setSubstance(normalize(value)));
        updateIfNotBlank(dto.reaction(), existing::setReaction);
        updateIfNotNull(dto.severity(), existing::setSeverity);
    }

    private <T> void updateIfNotNull(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }

    private void updateIfNotBlank(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }

    private String normalize(String value) {
        return value == null
                ? null
                : value.trim().toLowerCase();
    }


}