package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.AllergySeverity;

// TODO: add @NotNull validation - null = νέα, not null = υπάρχουσα
public record AllergyCreateDTO(
        String substance,
        AllergySeverity severity,
        String reaction
) {}
