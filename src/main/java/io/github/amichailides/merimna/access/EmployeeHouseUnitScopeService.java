package io.github.amichailides.merimna.access;

import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class EmployeeHouseUnitScopeService {

    public boolean canAccess(Employee employee, HouseUnit houseUnit) {
        return employee.getAccessibleHouseUnits(LocalDate.now())
                .contains(houseUnit);
    }

    public Set<HouseUnit> getAccessibleHouseUnits(Employee employee) {
        return employee.getAccessibleHouseUnits(LocalDate.now());
    }
}
