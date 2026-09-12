package io.github.amichailides.merimna.employeePosition.dto;

import io.github.amichailides.merimna.domain.Permission;
import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.Set;

@Schema(description = "Request payload for creating an employee position.")
@Builder
public record EmployeePositionCreateDTO(

        @Schema(description = "Unique employee position code.", example = "CAREGIVER")
        @NotBlank(message = "{employeePosition.code.notBlank}", groups = FirstOrder.class)
        @Size(max = 50, message = "{employeePosition.code.size}", groups = SecondOrder.class)
        @Pattern(
                regexp = ValidationPatterns.EMPLOYEE_POSITION_CODE,
                message = "{employeePosition.code.invalid}",
                groups = SecondOrder.class
        )
        String code,

        @Schema(description = "Human-readable employee position name.", example = "Caregiver")
        @NotBlank(message = "{employeePosition.displayName.notBlank}", groups = FirstOrder.class)
        @ValidGreekLatinText(
                message = "{employeePosition.displayName.invalid}",
                groups = SecondOrder.class
        )
        String displayName,

        @Schema(description = "Whether employees in this position require exclusive placement.", example = "false")
        @NotNull(message = "{employeePosition.requiresExclusivePlacement.notNull}", groups = FirstOrder.class)
        Boolean requiresExclusivePlacement,

        @Schema(description = "Permissions granted to this employee position.")
        @NotNull(message = "{employeePosition.permissions.notNull}", groups = FirstOrder.class)
        @NotEmpty(message = "{employeePosition.permissions.notEmpty}", groups = FirstOrder.class)
        Set<Permission> permissions
) {}
