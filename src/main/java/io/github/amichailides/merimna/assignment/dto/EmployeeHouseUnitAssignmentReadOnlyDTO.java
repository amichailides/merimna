package io.github.amichailides.merimna.assignment.dto;

import io.github.amichailides.merimna.assignment.AssignmentType;

import java.time.LocalDate;

public record EmployeeHouseUnitAssignmentReadOnlyDTO(
        Long id,
        String houseUnitCode,
        String houseUnitDisplayName,
        AssignmentType assignmentType,
        LocalDate startDate,
        LocalDate endDate
) {}
