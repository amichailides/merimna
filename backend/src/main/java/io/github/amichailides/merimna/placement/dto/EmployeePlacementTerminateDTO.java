package io.github.amichailides.merimna.placement.dto;

import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Schema(description = "Request payload for terminating an employee placement.")
@Builder
public record EmployeePlacementTerminateDTO(
        @Schema(description = "End date of the placement", example = "2026-04-26")
        @NotNull(message = "{placement.endDate.required}", groups = FirstOrder.class)
        LocalDate endDate
) {}
