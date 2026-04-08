package io.github.amichailides.merimna.employeePosition.dto;

import lombok.Builder;

@Builder
public record EmployeePositionReadOnlyDTO(
        String code,
        String displayName,
        Boolean requiresExclusivePlacement
) {}
