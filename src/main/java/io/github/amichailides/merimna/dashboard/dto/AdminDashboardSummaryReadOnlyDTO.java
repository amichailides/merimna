package io.github.amichailides.merimna.dashboard.dto;

public record AdminDashboardSummaryReadOnlyDTO(
        long activeEmployees,
        long activeBeneficiaries,
        long houseUnits,
        long activeAssignments,
        long activePlacements
) {}