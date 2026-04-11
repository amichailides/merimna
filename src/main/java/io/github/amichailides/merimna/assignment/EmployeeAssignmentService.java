package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;

import java.util.List;

public interface EmployeeAssignmentService {
    EmployeeAssignmentReadOnlyDTO create(Long employeeId, EmployeeAssignmentCreateDTO dto);

    void cancel(Long employeeId, Long assignmentId);

    List<EmployeeAssignmentReadOnlyDTO> getAssignments(Long employeeId, EmployeeAssignmentView view);

    void terminate(Long employeeId, Long assignmentId);
}
