package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.AllergySeverity;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// TODO: add @NotNull validation - null = νέα, not null = υπάρχουσα
public record AllergyCreateDTO(
        @NotBlank(message = "{allergy.substance.required}")
        @ValidGreekLatinText(max = 50, message = "{allergy.substance.invalid}")
        String substance,

        @NotNull(message = "{allergy.severity.required}")
        AllergySeverity severity,

        @NotBlank(message = "{allergy.reaction.required}")
        @ValidGreekLatinText(max = 200, message = "{allergy.reaction.invalid}")
        String reaction
) {}
