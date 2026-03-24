package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.domain.AllergySeverity;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import lombok.Builder;

@Builder
public record AllergyUpdateDTO(
        Long id,

        @ValidGreekLatinText(max = 50, message = "{allergy.substance.invalid}", groups = SecondOrder.class)
        String substance,

        AllergySeverity severity,

        @ValidGreekLatinText(max = 200, message = "{allergy.reaction.invalid}", groups = SecondOrder.class)
        String reaction
) {}
