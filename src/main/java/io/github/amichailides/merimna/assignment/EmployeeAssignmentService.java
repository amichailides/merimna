package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;

import java.util.List;
import java.util.UUID;

public interface EmployeeAssignmentService {
    EmployeeAssignmentReadOnlyDTO create(UUID employeePublicId, EmployeeAssignmentCreateDTO dto);

    void cancel(UUID employeePublicId, Long assignmentId);

    List<EmployeeAssignmentReadOnlyDTO> getAllAssignments(UUID employeePublicId, EmployeeAssignmentView view);

    void terminate(UUID employeePublicId, Long assignmentId);

    EmployeeAssignmentReadOnlyDTO getAssignmentById(UUID employeePublicId, Long assignmentId);
}
