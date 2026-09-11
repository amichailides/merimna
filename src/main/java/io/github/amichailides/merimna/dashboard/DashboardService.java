package io.github.amichailides.merimna.dashboard;

import io.github.amichailides.merimna.dashboard.dto.AdminDashboardReadOnlyDTO;

public interface DashboardService {

    AdminDashboardReadOnlyDTO getAdminDashboard();
}
