package io.github.amichailides.merimna.access;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.Role;
import io.github.amichailides.merimna.security.CurrentUserProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HouseUnitAccessServiceTest {

    @Mock private CurrentUserProvider currentUserProvider;
    @Mock private EmployeeHouseUnitScopeService scopeService;

    @InjectMocks
    private HouseUnitAccessService houseUnitAccessService;

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
    }
}