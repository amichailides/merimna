package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees/{employeeId}/assignments")
@Validated
@RequiredArgsConstructor
public class EmployeeAssignmentController {
    private final EmployeeAssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<EmployeeAssignmentReadOnlyDTO> create(
            @PathVariable @Positive(message = "{employee.id.positive}") Long employeeId,
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeAssignmentCreateDTO dto) {

        EmployeeAssignmentReadOnlyDTO assignment = assignmentService.create(employeeId, dto);
        return ResponseEntity
                .created(buildLocationUri(assignment.id()))
                .body(assignment);
    }

    @PostMapping("/{assignmentId}/cancel")
    public ResponseEntity<Void> cancelAssignment(
            @PathVariable @Positive(message = "{employee.id.positive}") Long employeeId,
            @PathVariable @Positive(message = "{assignment.id.positive}") Long assignmentId
    ) {
        assignmentService.cancel(employeeId, assignmentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{assignmentId}/terminate")
    public ResponseEntity<Void> terminateAssignment(
            @PathVariable @Positive(message = "{employee.id.positive}") Long employeeId,
            @PathVariable @Positive(message = "{assignment.id.positive}") Long assignmentId
    ) {
        assignmentService.terminate(employeeId, assignmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<EmployeeAssignmentReadOnlyDTO> getAllAssignments(
            @PathVariable @Positive(message = "{employee.id.positive}") Long employeeId,
            @RequestParam(defaultValue = "ACTIVE") EmployeeAssignmentView view) {

        return assignmentService.getAllAssignments(employeeId, view);
    }

    @GetMapping("/{assignmentId}")
    public EmployeeAssignmentReadOnlyDTO getAssignmentById(
            @PathVariable @Positive(message = "{employee.id.positive}") Long employeeId,
            @PathVariable @Positive(message = "{assignment.id.positive}") Long assignmentId) {

        return assignmentService.getAssignmentById(employeeId, assignmentId);
    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
