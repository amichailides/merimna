package io.github.amichailides.merimna.allergy.dto;

import io.github.amichailides.merimna.domain.AllergySeverity;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AllergyUpdateDTO(
        UUID publicId,

        @ValidGreekLatinText(extended = true, max = 50, message = "{allergy.substance.invalid}", groups = SecondOrder.class)
        String substance,

        AllergySeverity severity,

        @ValidGreekLatinText(extended = true, max = 200, message = "{allergy.reaction.invalid}", groups = SecondOrder.class)
        String reaction
) {}
