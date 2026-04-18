package io.github.amichailides.merimna.employeePosition.dto;

import io.github.amichailides.merimna.domain.Permission;
import lombok.Builder;

import java.util.Set;

@Builder
public record EmployeePositionReadOnlyDTO(
        String code,
        String displayName,
        Boolean requiresExclusivePlacement,
        Set<Permission> permissions
) {}
