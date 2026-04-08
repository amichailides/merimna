package io.github.amichailides.merimna.employee.dto;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployeeSearchDTO(
        @Schema(description = "Free-text search (first name, last name, email)", example = "giannis")
        @Size(min = 2, max = 100, message = "{employee.searchTerm.size}")
        String q,

        @Schema(description = "Employee position code", example = "CAREGIVER")
        String positionCode,

        @Schema(description = "Filter by assigned house unit code", example = "UNIT_A")
        @Pattern(
                regexp = ValidationPatterns.HOUSE_UNIT_CODE,
                message = "{employee.houseUnitCode.invalid}"
        )
        String houseUnit,

        @Schema(description = "Include inactive employees", example = "false")
        Boolean includeInactive
) {}
