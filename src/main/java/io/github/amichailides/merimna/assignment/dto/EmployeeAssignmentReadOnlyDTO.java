package io.github.amichailides.merimna.assignment.dto;

import io.github.amichailides.merimna.assignment.EmployeeAssignmentStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmployeeAssignmentReadOnlyDTO(
        Long id,
        String houseUnitCode,
        String houseUnitDisplayName,
        EmployeeAssignmentStatus status,
        LocalDate startDate,
        LocalDate endDate
) {}
