package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.AllergySeverity;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.SecondOrder;


public record AllergyUpdateDTO(
        Long id,

        @ValidGreekLatinText(max = 50, message = "{allergy.substance.invalid}", groups = SecondOrder.class)
        String substance,

        AllergySeverity severity,

        @ValidGreekLatinText(max = 200, message = "{allergy.reaction.invalid}", groups = SecondOrder.class)
        String reaction
) {}
