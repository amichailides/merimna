package io.github.amichailides.merimna.placement.dto;

import io.github.amichailides.merimna.domain.PlacementReason;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmployeePlacementReadOnlyDTO(
        Long id,
        Long employeeId,
        String houseUnitCode,
        String houseUnitDisplayName,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        PlacementReason reason,
        boolean active
) {}
