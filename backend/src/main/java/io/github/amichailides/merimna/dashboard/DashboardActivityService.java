package io.github.amichailides.merimna.dashboard;

import io.github.amichailides.merimna.dashboard.dto.DashboardActivityReadOnlyDTO;

import java.util.List;

public interface DashboardActivityService {

    List<DashboardActivityReadOnlyDTO> getRecentActivity();
}