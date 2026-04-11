package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.exception.AssignmentBeforeHireDateException;
import io.github.amichailides.merimna.assignment.exception.AssignmentOverlapNotAllowedException;
import io.github.amichailides.merimna.assignment.exception.DuplicateActiveAssignmentForHouseException;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.EmployeeAssignment;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.exception.EmployeeInactiveException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmployeeAssignmentPolicy {

    public void validateForCreate(
            Employee employee,
            HouseUnit houseUnit,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (!employee.isActive()) {
            throw new EmployeeInactiveException();
        }
        if (requiresExclusivePlacement(employee)
                && hasOverlappingAssignment(employee, startDate, endDate)) {
            throw new AssignmentOverlapNotAllowedException();
        }
        if (!validateNoActiveAssignmentForSameHouse(employee, houseUnit)) {
            throw new DuplicateActiveAssignmentForHouseException();
        }

        validateStartDateNotBeforeHireDate(employee.getHireDate(), startDate);
    }

    private boolean requiresExclusivePlacement(Employee employee) {
        return employee.getPosition().isRequiresExclusivePlacement();
    }

    private boolean hasOverlappingAssignment(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return employee.getAssignments().stream()
                .filter(EmployeeAssignment::isActive)
                .anyMatch(a -> a.overlapsWith(startDate, endDate));
    }

    private boolean validateNoActiveAssignmentForSameHouse(Employee employee, HouseUnit houseUnit) {
        return employee.getAssignments().stream()
                .noneMatch(a -> a.getStatus() == EmployeeAssignmentStatus.ACTIVE &&
                        houseUnit.getCode().equals(a.getHouseUnit().getCode())
                );
    }

    private void validateStartDateNotBeforeHireDate (LocalDate hireDate,LocalDate startDate) {
        if (hireDate.isAfter(startDate)) {
            throw new AssignmentBeforeHireDateException();
        }
    }
}