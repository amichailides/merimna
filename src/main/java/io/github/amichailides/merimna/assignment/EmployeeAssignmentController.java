package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.validation.annotations.ValidUUID;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.constraints.NotBlank;
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
                .created(buildLocationUri(assignment.publicId()))
                .body(assignment);
    }

    @PreAuthorize("hasAuthority('ASSIGNMENT_CANCEL')")
    @PostMapping("/{assignmentPublicId}/cancel")
    public ResponseEntity<Void> cancelAssignment(
            @PathVariable
            @NotBlank(message = "{employee.publicId.required}")
            @ValidUUID(message = "{employee.publicId.invalid}")
            String employeePublicId,
            @PathVariable UUID assignmentPublicId
    ) {
        assignmentService.cancel(UUID.fromString(employeePublicId), assignmentPublicId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ASSIGNMENT_TERMINATE')")
    @PostMapping("/{assignmentPublicId}/terminate")
    public ResponseEntity<Void> terminateAssignment(
            @PathVariable
            @NotBlank(message = "{employee.publicId.required}")
            @ValidUUID(message = "{employee.publicId.invalid}")
            String employeePublicId,
            @PathVariable UUID assignmentPublicId
    ) {
        assignmentService.terminate(UUID.fromString(employeePublicId), assignmentPublicId);
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
    @GetMapping("/{assignmentPublicId}")
    public EmployeeAssignmentReadOnlyDTO getAssignmentByPublicId(
            @PathVariable
            @NotBlank(message = "{employee.publicId.required}")
            @ValidUUID(message = "{employee.publicId.invalid}")
            String employeePublicId,
            @PathVariable UUID assignmentPublicId) {

        return assignmentService.getAssignmentByPublicId(UUID.fromString(employeePublicId), assignmentPublicId);
    }

    private URI buildLocationUri(UUID assignmentPublicId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{assignmentPublicId}")
                .buildAndExpand(assignmentPublicId)
                .toUri();
    }
}
