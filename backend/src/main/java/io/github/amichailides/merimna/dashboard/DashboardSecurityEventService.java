package io.github.amichailides.merimna.dashboard;

import io.github.amichailides.merimna.dashboard.dto.DashboardSecurityEventReadOnlyDTO;

import java.util.List;

public interface DashboardSecurityEventService {

    List<DashboardSecurityEventReadOnlyDTO> getRecentSecurityEvents();
}
