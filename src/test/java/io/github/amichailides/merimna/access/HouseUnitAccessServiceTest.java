package io.github.amichailides.merimna.access;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.domain.Role;
import io.github.amichailides.merimna.security.CurrentUserProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HouseUnitAccessServiceTest {

    @Mock private CurrentUserProvider currentUserProvider;
    @Mock private EmployeeHouseUnitScopeService scopeService;

    @InjectMocks
    private HouseUnitAccessService houseUnitAccessService;

    private static final UUID CURRENT_EMPLOYEE_PUBLIC_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID SUBJECT_EMPLOYEE_PUBLIC_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    private static final UUID SHARED_HOUSE_UNIT_PUBLIC_ID =
            UUID.fromString("33333333-3333-3333-3333-333333333333");

    private static final UUID OTHER_HOUSE_UNIT_PUBLIC_ID =
            UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Nested
    class EnsureCanAccessEmployee {

        @Test
        void shouldAllowAccess_whenCurrentUserIsAdmin() {
            Employee subjectEmployee = mock(Employee.class);
            when(currentUserProvider.getCurrentUserRole()).thenReturn(Role.ADMIN);

            houseUnitAccessService.ensureCanAccess(subjectEmployee);

            verify(currentUserProvider).getCurrentUserRole();
            verifyNoMoreInteractions(currentUserProvider);
            verifyNoInteractions(scopeService, subjectEmployee);
        }

        @Test
        void shouldAllowAccess_whenCurrentEmployeeSharesHouseUnitWithSubjectEmployee() {
            Employee currentEmployee = mock(Employee.class);
            Employee subjectEmployee = mock(Employee.class);

            HouseUnit currentEmployeeHouseUnit = mock(HouseUnit.class);
            HouseUnit subjectEmployeeHouseUnit = mock(HouseUnit.class);

            UUID sharedHouseUnitPublicId =
                    UUID.fromString("33333333-3333-3333-3333-333333333333");

            when(currentUserProvider.getCurrentUserRole())
                    .thenReturn(Role.STAFF);

            when(currentUserProvider.getCurrentEmployee())
                    .thenReturn(currentEmployee);

            when(scopeService.getAccessibleHouseUnits(currentEmployee))
                    .thenReturn(Set.of(currentEmployeeHouseUnit));

            when(subjectEmployee.getAccessibleHouseUnits(any(LocalDate.class)))
                    .thenReturn(Set.of(subjectEmployeeHouseUnit));

            when(currentEmployeeHouseUnit.getPublicId())
                    .thenReturn(sharedHouseUnitPublicId);

            when(subjectEmployeeHouseUnit.getPublicId())
                    .thenReturn(sharedHouseUnitPublicId);

            houseUnitAccessService.ensureCanAccess(subjectEmployee);

            verify(currentUserProvider).getCurrentUserRole();
            verify(currentUserProvider).getCurrentEmployee();
            verify(scopeService).getAccessibleHouseUnits(currentEmployee);
            verify(subjectEmployee).getAccessibleHouseUnits(any(LocalDate.class));
        }
    }

    @Test
    void shouldThrowAccessDeniedException_whenCurrentEmployeeDoesNotShareHouseUnitWithSubjectEmployee() {
        Employee currentEmployee = mock(Employee.class);
        Employee subjectEmployee = mock(Employee.class);

        HouseUnit currentEmployeeHouseUnit = mock(HouseUnit.class);
        HouseUnit subjectEmployeeHouseUnit = mock(HouseUnit.class);

        when(currentUserProvider.getCurrentUserRole())
                .thenReturn(Role.STAFF);

        when(currentUserProvider.getCurrentEmployee())
                .thenReturn(currentEmployee);

        when(scopeService.getAccessibleHouseUnits(currentEmployee))
                .thenReturn(Set.of(currentEmployeeHouseUnit));

        when(subjectEmployee.getAccessibleHouseUnits(any(LocalDate.class)))
                .thenReturn(Set.of(subjectEmployeeHouseUnit));

        when(currentEmployeeHouseUnit.getPublicId())
                .thenReturn(SHARED_HOUSE_UNIT_PUBLIC_ID);

        when(subjectEmployeeHouseUnit.getPublicId())
                .thenReturn(OTHER_HOUSE_UNIT_PUBLIC_ID);

        when(subjectEmployee.getPublicId())
                .thenReturn(SUBJECT_EMPLOYEE_PUBLIC_ID);

        assertThrows(
                AccessDeniedException.class,
                () -> houseUnitAccessService.ensureCanAccess(subjectEmployee)
        );

        verify(currentUserProvider).getCurrentUserRole();
        verify(currentUserProvider).getCurrentEmployee();
        verify(scopeService).getAccessibleHouseUnits(currentEmployee);
        verify(subjectEmployee).getAccessibleHouseUnits(any(LocalDate.class));
        verify(subjectEmployee).getPublicId();
    }
}
