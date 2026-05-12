package io.github.amichailides.merimna.employee.dto;

import io.github.amichailides.merimna.validation.annotations.OptionalNotBlank;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Search filters for employees. All fields are optional.")
public record EmployeeSearchDTO(
        @Schema(description = "Free-text search (first name, last name, email)", example = "giannis")
        @OptionalNotBlank(message = "{employee.searchTerm.blank}", groups = FirstOrder.class)
        @Size(min = 2, max = 100, message = "{employee.searchTerm.size}", groups = SecondOrder.class)
        String q,

        @Schema(description = "Employee position code", example = "CAREGIVER")
        @OptionalNotBlank(message = "{employee.positionCode.blank}", groups = FirstOrder.class)
        String positionCode,

        @Schema(
                description = "Filter by assigned house unit public identifier, usually selected from a UI dropdown.",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID houseUnitPublicId,

        @Schema(description = "Include inactive employees", example = "false")
        Boolean includeInactive
) {}
