package io.github.amichailides.merimna.dashboard.dto;

import java.util.List;

public record AdminDashboardReadOnlyDTO(
        AdminDashboardSummaryReadOnlyDTO summary,
        List<HouseUnitOverviewReadOnlyDTO> houseUnits,
        List<DashboardActivityReadOnlyDTO> recentActivity,
        List<DashboardSecurityEventReadOnlyDTO> securityEvents
) {}
