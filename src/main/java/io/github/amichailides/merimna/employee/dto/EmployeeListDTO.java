package io.github.amichailides.merimna.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record EmployeeListDTO(
        @Schema(description = "Employee ID", example = "1")
        Long id,

        @Schema(description = "First name", example = "Giannis")
        String firstName,

        @Schema(description = "Last name", example = "Papadopoulos")
        String lastName,

        @Schema(description = "Employee position code", example = "CAREGIVER")
        String positionCode,

        @Schema(description = "Indicates whether the employee is active", example = "true")
        boolean active
) {}
