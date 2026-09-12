package io.github.amichailides.merimna.assignment.dto;

import io.github.amichailides.merimna.assignment.EmployeeAssignmentStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record EmployeeAssignmentReadOnlyDTO(
        UUID publicId,
        UUID houseUnitPublicId,
        String houseUnitDisplayName,
        EmployeeAssignmentStatus status,
        LocalDate startDate,
        LocalDate endDate
) {}
