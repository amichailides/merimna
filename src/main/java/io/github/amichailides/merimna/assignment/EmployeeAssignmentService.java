package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;

import java.util.List;

public interface EmployeeAssignmentService {
    EmployeeAssignmentReadOnlyDTO create(String employeePublicId, EmployeeAssignmentCreateDTO dto);

    void cancel(String employeePublicId, Long assignmentId);

    List<EmployeeAssignmentReadOnlyDTO> getAllAssignments(String employeePublicId, EmployeeAssignmentView view);

    void terminate(String employeePublicId, Long assignmentId);

    EmployeeAssignmentReadOnlyDTO getAssignmentById(String employeePublicId, Long assignmentId);
}
