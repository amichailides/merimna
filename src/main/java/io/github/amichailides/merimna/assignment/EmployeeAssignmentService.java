package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;

public interface EmployeeAssignmentService {
    EmployeeAssignmentReadOnlyDTO create(Long employeeId, EmployeeAssignmentCreateDTO dto);
}
