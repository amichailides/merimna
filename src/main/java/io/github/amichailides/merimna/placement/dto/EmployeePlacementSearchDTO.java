package io.github.amichailides.merimna.placement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Builder;

import java.util.UUID;

@Builder
public record EmployeePlacementSearchDTO(
        @Schema(description = "Filter by employee public ID")
        UUID employeePublicId,

        @Schema(description = "Filter by house unit public ID")
        UUID houseUnitPublicId,

        @Schema(description = "Include inactive placements")
        Boolean includeInactive,

        @Schema(description = "Filter by start date range")
        @Valid
        DateRange startDateRange,

        @Schema(description = "Filter by end date range")
        @Valid
        DateRange endDateRange
) {}