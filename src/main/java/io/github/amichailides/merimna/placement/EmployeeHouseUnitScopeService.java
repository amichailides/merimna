package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class EmployeeHouseUnitScopeService {

    public boolean hasActiveAccessTo(Employee employee, HouseUnit houseUnit) {
        return employee.getAccessibleHouseUnits(LocalDate.now())
                .contains(houseUnit);
    }

    public Set<HouseUnit> getAccessibleHouseUnits(Employee employee) {
        return employee.getAccessibleHouseUnits(LocalDate.now());
    }
}
