package io.github.amichailides.merimna.allergy.dto;

import io.github.amichailides.merimna.domain.AllergySeverity;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AllergyReadOnlyDTO(
        UUID publicId,
        String substance,
        AllergySeverity severity,
        String reaction
) {}