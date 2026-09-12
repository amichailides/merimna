package io.github.amichailides.merimna.assignment.dto;

import io.github.amichailides.merimna.validation.AssignmentDateRange;
import io.github.amichailides.merimna.validation.annotations.ValidAssignmentDateRange;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

@ValidAssignmentDateRange(groups = SecondOrder.class)
public record EmployeeAssignmentCreateDTO(
        @NotNull(message = "{houseUnit.publicId.notNull}", groups = FirstOrder.class)
        UUID houseUnitPublicId,

        @NotNull(message = "{assignment.startDate.required}", groups = FirstOrder.class)
        @FutureOrPresent(message = "{assignment.startDate.futureOrPresent}", groups = SecondOrder.class)
        LocalDate startDate,

        @FutureOrPresent(message = "{assignment.endDate.futureOrPresent}", groups = SecondOrder.class)
        LocalDate endDate
) implements AssignmentDateRange {}
