package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.exception.AssignmentPolicyViolationException;
import io.github.amichailides.merimna.domain.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmployeeAssignmentPolicy {

    public void validateForCreate(
            Employee employee,
            AssignmentType assignmentType,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (assignmentType == AssignmentType.PRIMARY
                && requiresExclusivePlacement(employee)
                && hasOverlappingAssignment(employee, AssignmentType.PRIMARY, startDate, endDate)) {
            throw new AssignmentPolicyViolationException();
        }

        if (assignmentType == AssignmentType.TEMPORARY_COVERAGE
                && requiresExclusivePlacement(employee)
                && hasOverlappingAssignment(employee, AssignmentType.TEMPORARY_COVERAGE, startDate, endDate)) {
            throw new AssignmentPolicyViolationException();
        }
    }

    private boolean requiresExclusivePlacement(Employee employee) {
        return switch (employee.getPosition()) {
            case CAREGIVER, HOUSE_MANAGER, EDUCATOR -> true;
            default -> false;
        };
    }

    private boolean hasOverlappingAssignment(
            Employee employee,
            AssignmentType assignmentType,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return employee.getAssignments().stream()
                .filter(assignment -> assignment.getAssignmentType() == assignmentType)
                .anyMatch(assignment -> assignment.overlapsWith(startDate, endDate));
    }
}