package io.github.amichailides.merimna.placement.dto;

import io.github.amichailides.merimna.domain.PlacementReason;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record EmployeePlacementReadOnlyDTO(
        UUID publicId,
        String houseUnitCode,
        String houseUnitDisplayName,
        LocalDate startDate,
        LocalDate endDate,
        PlacementReason reason,
        boolean active
) {}
