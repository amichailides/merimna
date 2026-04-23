package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.validation.annotations.ValidUUID;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees/{employeePublicId}/assignments")
@Validated
@RequiredArgsConstructor
public class EmployeeAssignmentController {
    private final EmployeeAssignmentService assignmentService;

    @PreAuthorize("hasAuthority('ASSIGNMENT_CREATE')")
    @PostMapping
    public ResponseEntity<EmployeeAssignmentReadOnlyDTO> create(
            @PathVariable
            @NotBlank(message = "{employee.publicId.required}")
            @ValidUUID(message = "{employee.publicId.invalid}")
            String employeePublicId,
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeAssignmentCreateDTO dto) {

        EmployeeAssignmentReadOnlyDTO assignment = assignmentService.create(UUID.fromString(employeePublicId), dto);
        return ResponseEntity
                .created(buildLocationUri(assignment.id()))
                .body(assignment);
    }

    @PreAuthorize("hasAuthority('ASSIGNMENT_CANCEL')")
    @PostMapping("/{assignmentId}/cancel")
    public ResponseEntity<Void> cancelAssignment(
            @PathVariable
            @NotBlank(message = "{employee.publicId.required}")
            @ValidUUID(message = "{employee.publicId.invalid}")
            String employeePublicId,
            @PathVariable @Positive(message = "{assignment.id.positive}") Long assignmentId
    ) {
        assignmentService.cancel(UUID.fromString(employeePublicId), assignmentId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ASSIGNMENT_TERMINATE')")
    @PostMapping("/{assignmentId}/terminate")
    public ResponseEntity<Void> terminateAssignment(
            @PathVariable
            @NotBlank(message = "{employee.publicId.required}")
            @ValidUUID(message = "{employee.publicId.invalid}")
            String employeePublicId,
            @PathVariable @Positive(message = "{assignment.id.positive}") Long assignmentId
    ) {
        assignmentService.terminate(UUID.fromString(employeePublicId), assignmentId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ASSIGNMENT_READ')")
    @GetMapping
    public List<EmployeeAssignmentReadOnlyDTO> getAllAssignments(
            @PathVariable
            @NotBlank(message = "{employee.publicId.required}")
            @ValidUUID(message = "{employee.publicId.invalid}")
            String employeePublicId,
            @RequestParam(defaultValue = "ACTIVE") EmployeeAssignmentView view) {

        return assignmentService.getAllAssignments(UUID.fromString(employeePublicId), view);
    }

    @PreAuthorize("hasAuthority('ASSIGNMENT_READ')")
    @GetMapping("/{assignmentId}")
    public EmployeeAssignmentReadOnlyDTO getAssignmentById(
            @PathVariable
            @NotBlank(message = "{employee.publicId.required}")
            @ValidUUID(message = "{employee.publicId.invalid}")
            String employeePublicId,
            @PathVariable @Positive(message = "{assignment.id.positive}") Long assignmentId) {

        return assignmentService.getAssignmentById(UUID.fromString(employeePublicId), assignmentId);
    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
