package io.github.amichailides.merimna.placement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Builder;

import java.util.UUID;

@Schema(description = "Search filters for employee placements. All fields are optional.")
@Builder
public record EmployeePlacementSearchDTO(
        @Schema(
                description = "Filter by employee public identifier.",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID employeePublicId,

        @Schema(
                description = "Filter by house unit public identifier, usually selected from a UI dropdown.",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID houseUnitPublicId,

        @Schema(description = "Whether inactive placements should be included in the results.", example = "false")
        Boolean includeInactive,

        @Schema(description = "Filter by placement start date range.")
        @Valid
        DateRange startDateRange,

        @Schema(description = "Filter by placement end date range.")
        @Valid
        DateRange endDateRange
) {}