package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.exception.AssignmentBeforeHireDateException;
import io.github.amichailides.merimna.assignment.exception.AssignmentOverlapNotAllowedException;
import io.github.amichailides.merimna.assignment.exception.DuplicateActiveAssignmentForHouseException;
import io.github.amichailides.merimna.domain.Employee;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.employee.exception.EmployeeInactiveException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EmployeeAssignmentPolicy {

    private final EmployeeAssignmentRepository employeeAssignmentRepository;

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
        if (hasActiveAssignmentForSameHouse(employee, houseUnit)) {
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
        LocalDate effectiveEndDate = endDate != null ? endDate : LocalDate.MAX;
        return employeeAssignmentRepository.existsOverlappingAssignment(
                employee, EmployeeAssignmentStatus.ACTIVE, startDate, effectiveEndDate
        );
    }

    private boolean hasActiveAssignmentForSameHouse(Employee employee, HouseUnit houseUnit) {
        return employeeAssignmentRepository.existsByEmployeeAndHouseUnitAndStatus(
                employee, houseUnit, EmployeeAssignmentStatus.ACTIVE
        );
    }

    private void validateStartDateNotBeforeHireDate (LocalDate hireDate,LocalDate startDate) {
        if (hireDate.isAfter(startDate)) {
            throw new AssignmentBeforeHireDateException();
        }
    }
}