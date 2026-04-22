package io.github.amichailides.merimna.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record EmployeeListDTO(
        @Schema(description = "Public identifier of the employee", example = "550e8400-e29b-41d4-a716-446655440000")
        String publicId,

        @Schema(description = "First name", example = "Giannis")
        String firstName,

        @Schema(description = "Last name", example = "Papadopoulos")
        String lastName,

        @Schema(description = "Employee position code", example = "CAREGIVER")
        String positionCode,

        @Schema(description = "Indicates whether the employee is active", example = "true")
        boolean active
) {}
