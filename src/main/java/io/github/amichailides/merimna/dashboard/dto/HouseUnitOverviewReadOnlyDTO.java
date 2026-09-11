package io.github.amichailides.merimna.dashboard.dto;

import java.util.UUID;

public record HouseUnitOverviewReadOnlyDTO(
        UUID publicId,
        String code,
        String displayName,
        int maxCapacity,
        long activeBeneficiaries,
        long activeAssignedEmployees,
        long activePlacedEmployees
) {}