package io.github.amichailides.merimna.employeePosition.dto;

import io.github.amichailides.merimna.domain.Permission;
import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

@Builder
public record EmployeePositionCreateDTO(

        @NotBlank(message = "{employeePosition.code.notBlank}", groups = FirstOrder.class)
        @Size(max = 50, message = "{employeePosition.code.size}", groups = FirstOrder.class)
        @Pattern(
                regexp = ValidationPatterns.EMPLOYEE_POSITION_CODE,
                message = "{employeePosition.code.invalid}",
                groups = SecondOrder.class
        )
        String code,

        @NotBlank(message = "{employeePosition.displayName.notBlank}", groups = FirstOrder.class)
        @Size(max = 100, message = "{employeePosition.displayName.size}", groups = FirstOrder.class)
        String displayName,

        @NotNull(message = "{employeePosition.requiresExclusivePlacement.notNull}", groups = FirstOrder.class)
        Boolean requiresExclusivePlacement,

        @NotNull(message = "{employeePosition.permissions.notNull}", groups = FirstOrder.class)
        Set<Permission> permissions
) {}
