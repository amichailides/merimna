package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.AllergySeverity;
import lombok.Builder;

// TODO: add @NotNull validation - null = νέα, not null = υπάρχουσα
public record AllergyReadOnlyDTO(
        Long id,
        String substance,
        AllergySeverity severity,
        String reaction
) {}