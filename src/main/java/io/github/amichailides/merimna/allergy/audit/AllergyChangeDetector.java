package io.github.amichailides.merimna.allergy.audit;

import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.audit.EntityChangeSet;
import io.github.amichailides.merimna.domain.Allergy;
import org.springframework.stereotype.Component;

@Component
public class AllergyChangeDetector {

    public EntityChangeSet detectChanges(Allergy allergy,
                                         AllergyUpdateDTO dto) {

        return EntityChangeSet.builder()
                .trackIfPresent("substance", allergy.getSubstance(), normalize(dto.substance()))
                .trackIfPresent("severity", allergy.getSeverity(), dto.severity())
                .trackIfPresent("reaction", allergy.getReaction(), dto.reaction())
                .build();
    }

    private String normalize(String value) {
        return value == null
                ? null
                : value.trim().toLowerCase();
    }
}
