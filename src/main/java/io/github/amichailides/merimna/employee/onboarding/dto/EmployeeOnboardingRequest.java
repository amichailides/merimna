package io.github.amichailides.merimna.employee.onboarding.dto;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record EmployeeOnboardingRequest(

        @NotNull(
                message = "{employee.onboarding.employee.required}",
                groups = FirstOrder.class
        )
        @Valid
        EmployeeCreateDTO employee,

        @NotNull(
                message = "{employee.onboarding.initialAssignment.required}",
                groups = FirstOrder.class
        )
        @Valid
        EmployeeAssignmentCreateDTO initialAssignment,

        boolean grantSystemAccess
) {}
