package io.github.amichailides.merimna.assignment.dto;

import io.github.amichailides.merimna.assignment.AssignmentType;
import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidAssignmentDateRange;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@ValidAssignmentDateRange(groups = SecondOrder.class)
public record EmployeeHouseUnitAssignmentCreateDTO(
        @NotBlank(message = "{houseUnit.code.required}", groups = FirstOrder.class)
        @Pattern(
                regexp = ValidationPatterns.HOUSE_UNIT_CODE,
                message = "{houseUnit.code.invalid}",
                groups = SecondOrder.class
        )
        String houseUnitCode,

        @NotNull(message = "{assignment.type.required}", groups = FirstOrder.class)
        AssignmentType assignmentType,

        @NotNull(message = "{assignment.startDate.required}", groups = FirstOrder.class)
        @FutureOrPresent(message = "{assignment.startDate.futureOrPresent}", groups = SecondOrder.class)
        LocalDate startDate,

        @FutureOrPresent(message = "{assignment.endDate.futureOrPresent}", groups = SecondOrder.class)
        LocalDate endDate
) {}
