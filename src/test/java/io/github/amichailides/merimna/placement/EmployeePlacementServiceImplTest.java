package io.github.amichailides.merimna.placement;


import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeePlacement;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.domain.PlacementReason;
import io.github.amichailides.merimna.employee.EmployeeRepository;
import io.github.amichailides.merimna.houseunit.HouseUnitRepository;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class EmployeePlacementServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private HouseUnitRepository houseUnitRepository;

    @Mock
    private EmployeePlacementRepository placementRepository;

    @Mock
    private EmployeePlacementMapper placementMapper;

    @Mock
    private EmployeePlacementValidator placementValidator;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private EmployeePlacementServiceImpl placementService;

    private static final Long PLACEMENT_ID = 1L;
    private static final Long EMPLOYEE_ID = 2L;
    private static final Long HOUSE_UNIT_ID = 3L;

    private static final UUID PLACEMENT_PUBLIC_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID EMPLOYEE_PUBLIC_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID HOUSE_UNIT_PUBLIC_ID =
            UUID.fromString("33333333-3333-3333-3333-333333333333");

    private static final String HOUSE_UNIT_CODE = "UNIT_A";
    private static final String HOUSE_UNIT_DISPLAY_NAME = "House Unit A";

    private static final LocalDate START_DATE = LocalDate.of(2026, 4, 21);
    private static final LocalDate END_DATE = LocalDate.of(2026, 12, 31);

    private static final PlacementReason PLACEMENT_REASON = PlacementReason.TEMPORARY_COVERAGE;

    @Nested
    class GetByPublicIdTests {

        @Test
        void shouldGetPlacementByPublicId() {
            EmployeePlacement placement = defaultPlacement();
            EmployeePlacementReadOnlyDTO expectedDto = defaultReadOnlyDTO();

            when(placementRepository.findByPublicId(PLACEMENT_PUBLIC_ID))
                    .thenReturn(Optional.of(placement));
            when(placementMapper.toReadOnlyDTO(placement))
                    .thenReturn(expectedDto);

            EmployeePlacementReadOnlyDTO result =
                    placementService.getByPublicId(PLACEMENT_PUBLIC_ID);

            assertThat(result).isEqualTo(expectedDto);

            verify(placementRepository).findByPublicId(PLACEMENT_PUBLIC_ID);
            verify(placementMapper).toReadOnlyDTO(placement);
        }
    }

    private static EmployeePlacement defaultPlacement() {
        EmployeePlacement placement = EmployeePlacement.create(
                defaultEmployee(),
                defaultHouseUnit(),
                START_DATE,
                END_DATE,
                PLACEMENT_REASON
        );

        placement.setId(PLACEMENT_ID);
        placement.setPublicId(PLACEMENT_PUBLIC_ID);

        return placement;
    }

    private static Employee defaultEmployee() {
        return Employee.builder()
                .id(EMPLOYEE_ID)
                .publicId(EMPLOYEE_PUBLIC_ID)
                .firstName("Maria")
                .lastName("Konstantinou")
                .contactEmail("mkonstantinou@example.com")
                .mobileNumber("6945678901")
                .hireDate(LocalDate.of(2024, 1, 10))
                .build();
    }

    private static HouseUnit defaultHouseUnit() {
        return HouseUnit.builder()
                .id(HOUSE_UNIT_ID)
                .publicId(HOUSE_UNIT_PUBLIC_ID)
                .code(HOUSE_UNIT_CODE)
                .displayName(HOUSE_UNIT_DISPLAY_NAME)
                .address("Patision 101, Athens")
                .maxCapacity(10)
                .build();
    }

    private static EmployeePlacementReadOnlyDTO defaultReadOnlyDTO() {
        return EmployeePlacementReadOnlyDTO.builder()
                .publicId(PLACEMENT_PUBLIC_ID)
                .houseUnitCode(HOUSE_UNIT_CODE)
                .houseUnitDisplayName(HOUSE_UNIT_DISPLAY_NAME)
                .startDate(START_DATE)
                .endDate(END_DATE)
                .reason(PLACEMENT_REASON)
                .active(true)
                .build();
    }


}