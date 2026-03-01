package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.AllergySeverity;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import jakarta.validation.constraints.NotNull;

// TODO: add @NotNull validation - null = νέα, not null = υπάρχουσα
public record AllergyUpdateDTO(

        Long id,

        @ValidGreekLatinText(max = 50, message = "{allergy.substance.invalid}")
        String substance,

        AllergySeverity severity,

        @ValidGreekLatinText(max = 200, message = "{allergy.reaction.invalid}")
        String reaction
) {}
