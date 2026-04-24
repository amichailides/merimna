package io.github.amichailides.merimna.placement.dto;

import io.github.amichailides.merimna.domain.PlacementReason;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record EmployeePlacementReadOnlyDTO(
        UUID publicId,
        String houseUnitCode,
        String houseUnitDisplayName,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        PlacementReason reason,
        boolean active
) {}
