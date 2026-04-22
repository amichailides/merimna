package io.github.amichailides.merimna.placement.dto;

import io.github.amichailides.merimna.domain.PlacementReason;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EmployeePlacementCreateDTO(

        @Schema(description = "Public identifier of the employee to be placed", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "{placement.employeeId.notNull}", groups = FirstOrder.class)
        String employeePublicId,

        @Schema(description = "House unit business code.", example = "HU-TH-01")
        @NotBlank(message = "{houseUnit.code.required}", groups = FirstOrder.class)
        @Size(min = 2, max = 50, message = "{placement.houseUnitCode.size}", groups = SecondOrder.class)
        String houseUnitCode,

        @Schema(description = "Placement start datetime.", example = "2026-04-21T10:30:00")
        @NotNull(message = "{placement.startDateTime.notNull}", groups = FirstOrder.class)
        LocalDateTime startDateTime,

        @Schema(
                description = "Placement end datetime. Nullable for open placement.",
                example = "2026-04-21T18:00:00",
                nullable = true
        )
        LocalDateTime endDateTime,

        @Schema(description = "Reason for the placement.", example = "TEMPORARY_COVERAGE")
        @NotNull(message = "{placement.reason.notNull}", groups = FirstOrder.class)
        PlacementReason reason
) {}
