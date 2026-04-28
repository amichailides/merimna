package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.domain.Beneficiary;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.placement.EmployeeHouseUnitScopeService;
import io.github.amichailides.merimna.security.CurrentUserProvider;
import io.github.amichailides.merimna.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BeneficiaryAccessService {

    private final CurrentUserProvider currentUserProvider;
    private final EmployeeHouseUnitScopeService scopeService;

    public void checkCanAccess(Beneficiary beneficiary) {
        checkCanAccess(beneficiary.getHouseUnit());
    }

    public void checkCanAccess(HouseUnit houseUnit) {
        if (isUnrestricted()) {
            return;
        }

        Employee employee = currentUserProvider.getCurrentEmployee();

        if (!scopeService.hasActiveAccessTo(employee, houseUnit)) {
            throw new AccessDeniedException("No access to this beneficiary");
        }
    }

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