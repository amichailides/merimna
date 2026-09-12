package io.github.amichailides.merimna.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Available employee status filters for employee search.")
public enum EmployeeStatusFilter {
    @Schema(description = "Include both active and inactive employees.")
    ALL,

    @Schema(description = "Include only active employees.")
    ACTIVE,

    @Schema(description = "Include only inactive employees.")
    INACTIVE
}