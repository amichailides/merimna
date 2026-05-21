package io.github.amichailides.merimna.access;

import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.security.CurrentUserProvider;
import io.github.amichailides.merimna.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HouseUnitAccessService {

    private final CurrentUserProvider currentUserProvider;
    private final EmployeeHouseUnitScopeService scopeService;

    public void ensureCanAccess(Beneficiary beneficiary) {
        ensureCanAccess(beneficiary.getHouseUnit());
    }

    public void ensureCanAccess(HouseUnit houseUnit) {
        if (isUnrestricted()) {
            return;
        }

        Employee employee = currentUserProvider.getCurrentEmployee();

        if (!scopeService.canAccess(employee, houseUnit)) {
            throw new AccessDeniedException("No access to this house unit");
        }
    }

    /**
     * Resolves the house units that should limit beneficiary access for the current user.
     *
     * <p>Returns {@link Optional#empty()} when the current user has unrestricted access,
     * meaning no house-unit filter should be applied.</p>
     *
     * <p>Returns a set of accessible house units for restricted users, based on their
     * active assignments and placements.</p>
     */
    public Optional<Set<HouseUnit>> resolveHouseUnitScope() {
        if (isUnrestricted()) {
            return Optional.empty();
        }

        Employee employee = currentUserProvider.getCurrentEmployee();
        return Optional.of(scopeService.getAccessibleHouseUnits(employee));
    }

    private boolean isUnrestricted() {
        return currentUserProvider.getCurrentUserRole() == Role.ADMIN;
    }
}