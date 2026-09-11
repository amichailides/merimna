package io.github.amichailides.merimna.dashboard;

import io.github.amichailides.merimna.assignment.EmployeeAssignmentRepository;
import io.github.amichailides.merimna.assignment.EmployeeAssignmentStatus;
import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import io.github.amichailides.merimna.common.projection.HouseUnitCountProjection;
import io.github.amichailides.merimna.dashboard.dto.AdminDashboardReadOnlyDTO;
import io.github.amichailides.merimna.dashboard.dto.AdminDashboardSummaryReadOnlyDTO;
import io.github.amichailides.merimna.dashboard.dto.DashboardActivityReadOnlyDTO;
import io.github.amichailides.merimna.dashboard.dto.HouseUnitOverviewReadOnlyDTO;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.placement.EmployeePlacementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final HouseUnitRepository houseUnitRepository;
    private final EmployeeAssignmentRepository assignmentRepository;
    private final EmployeePlacementRepository placementRepository;
    private final DashboardActivityService dashboardActivityService;

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardReadOnlyDTO getAdminDashboard() {
        LocalDate today = LocalDate.now();

        AdminDashboardSummaryReadOnlyDTO summary = buildSummary(today);

        List<HouseUnitOverviewReadOnlyDTO> houseUnits =
                buildHouseUnitOverview(today);

        List<DashboardActivityReadOnlyDTO> recentActivity =
                dashboardActivityService.getRecentActivity();

        return new AdminDashboardReadOnlyDTO(
                summary,
                houseUnits,
                recentActivity
        );
    }

    private AdminDashboardSummaryReadOnlyDTO buildSummary(LocalDate today) {
        return new AdminDashboardSummaryReadOnlyDTO(
                employeeRepository.countByIsActiveTrue(),
                beneficiaryRepository.countByIsActiveTrue(),
                houseUnitRepository.count(),
                assignmentRepository.countActiveAssignments(
                        EmployeeAssignmentStatus.ACTIVE,
                        today
                ),
                placementRepository.countActivePlacements(today)
        );
    }

    private List<HouseUnitOverviewReadOnlyDTO> buildHouseUnitOverview(
            LocalDate today
    ) {
        Map<UUID, Long> beneficiariesByHouseUnit =
                toCountMap(
                        beneficiaryRepository.countActiveBeneficiariesByHouseUnit()
                );

        Map<UUID, Long> assignmentsByHouseUnit =
                toCountMap(
                        assignmentRepository.countActiveAssignmentsByHouseUnit(
                                EmployeeAssignmentStatus.ACTIVE,
                                today
                        )
                );

        Map<UUID, Long> placementsByHouseUnit =
                toCountMap(
                        placementRepository.countActivePlacementsByHouseUnit(today)
                );

        return houseUnitRepository.findAll(Sort.by("code")).stream()
                .map(houseUnit -> toOverviewDTO(
                        houseUnit,
                        beneficiariesByHouseUnit,
                        assignmentsByHouseUnit,
                        placementsByHouseUnit
                ))
                .toList();
    }

    private HouseUnitOverviewReadOnlyDTO toOverviewDTO(
            HouseUnit houseUnit,
            Map<UUID, Long> beneficiariesByHouseUnit,
            Map<UUID, Long> assignmentsByHouseUnit,
            Map<UUID, Long> placementsByHouseUnit
    ) {
        UUID houseUnitPublicId = houseUnit.getPublicId();

        return new HouseUnitOverviewReadOnlyDTO(
                houseUnitPublicId,
                houseUnit.getCode(),
                houseUnit.getDisplayName(),
                houseUnit.getMaxCapacity(),
                beneficiariesByHouseUnit.getOrDefault(houseUnitPublicId, 0L),
                assignmentsByHouseUnit.getOrDefault(houseUnitPublicId, 0L),
                placementsByHouseUnit.getOrDefault(houseUnitPublicId, 0L)
        );
    }

    private Map<UUID, Long> toCountMap(
            List<HouseUnitCountProjection> projections
    ) {
        return projections.stream()
                .collect(Collectors.toMap(
                        HouseUnitCountProjection::getHouseUnitPublicId,
                        HouseUnitCountProjection::getCount
                ));
    }
}
