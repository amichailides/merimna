package io.github.amichailides.merimna.assignment.dto;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidAssignmentDateRange;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@ValidAssignmentDateRange(groups = SecondOrder.class)
public record EmployeeAssignmentCreateDTO(
        @NotBlank(message = "{houseUnit.code.required}", groups = FirstOrder.class)
        @Pattern(
                regexp = ValidationPatterns.HOUSE_UNIT_CODE,
                message = "{houseUnit.code.invalid}",
                groups = SecondOrder.class
        )
        String houseUnitCode,

        @NotNull(message = "{assignment.startDate.required}", groups = FirstOrder.class)
        @PastOrPresent(message = "{assignment.startDate.futureOrPresent}", groups = SecondOrder.class)
        LocalDate startDate,

        @FutureOrPresent(message = "{assignment.endDate.futureOrPresent}", groups = SecondOrder.class)
        LocalDate endDate
) {}
