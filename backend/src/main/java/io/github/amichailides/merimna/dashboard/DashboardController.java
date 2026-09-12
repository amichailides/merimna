package io.github.amichailides.merimna.dashboard;

import io.github.amichailides.merimna.dashboard.dto.AdminDashboardReadOnlyDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(
        name = "Dashboard",
        description = "Organization dashboard overview"
)
public class DashboardController {

    private final DashboardService dashboardService;

    @PreAuthorize("hasAuthority('DASHBOARD_READ')")
    @GetMapping("/admin")
    public AdminDashboardReadOnlyDTO getAdminDashboard() {
        return dashboardService.getAdminDashboard();
    }
}
