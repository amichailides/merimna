package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees/{employeeId}/assignments")
@Validated
@RequiredArgsConstructor
public class EmployeeAssignmentController {
    private final EmployeeAssignmentService service;

    @PostMapping
    public ResponseEntity<EmployeeAssignmentReadOnlyDTO> create (
            @PathVariable @Positive Long employeeId,
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeAssignmentCreateDTO dto) {

        EmployeeAssignmentReadOnlyDTO result = service.create(employeeId, dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{assignmentId}/cancel")
    public ResponseEntity<Void> cancelAssignment(
            @PathVariable @Positive(message = "{employee.id.positive}") Long employeeId,
            @PathVariable @Positive(message = "{assignment.id.positive}") Long assignmentId
    ) {
        service.cancel(employeeId, assignmentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{assignmentId}/terminate")
    public ResponseEntity<Void> terminateAssignment (
            @PathVariable @Positive(message = "{employee.id.positive}") Long employeeId,
            @PathVariable @Positive(message = "{assignment.id.positive}") Long assignmentId
    ) {
        service.terminate(employeeId, assignmentId);
        return ResponseEntity.noContent().build();
    }
}
