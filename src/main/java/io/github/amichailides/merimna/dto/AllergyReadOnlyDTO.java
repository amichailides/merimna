package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.domain.AllergySeverity;
import lombok.Builder;

@Builder
public record AllergyReadOnlyDTO(
        Long id,
        String substance,
        AllergySeverity severity,
        String reaction
) {}