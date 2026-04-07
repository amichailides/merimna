package io.github.amichailides.merimna.assignment.dto;

import io.github.amichailides.merimna.assignment.AssignmentType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmployeeAssignmentReadOnlyDTO(
        Long id,
        String houseUnitCode,
        String houseUnitDisplayName,
        AssignmentType assignmentType,
        LocalDate startDate,
        LocalDate endDate
) {}
