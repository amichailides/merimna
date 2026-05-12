package io.github.amichailides.merimna.employee.dto;

import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(name = "EmployeeTerminateDTO", description = "Request payload for terminating an employee")
public record EmployeeTerminateDTO(

        @Schema(
                description = "Employee termination date",
                example = "2026-04-11",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "{employee.terminationDate.required}", groups = FirstOrder.class)
        LocalDate terminationDate
) {}