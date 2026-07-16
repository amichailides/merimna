package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentCreateDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequiredArgsConstructor
@Tag(
        name = "Employee Assignments",
        description = "Manage employees' official assignments to house units"
)
public class EmployeeAssignmentController {
    private final EmployeeAssignmentService assignmentService;

    @PreAuthorize("hasAuthority('ASSIGNMENT_CREATE')")
    @PostMapping
    public ResponseEntity<EmployeeAssignmentReadOnlyDTO> create(
            @PathVariable UUID employeePublicId,
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeAssignmentCreateDTO dto) {

        EmployeeAssignmentReadOnlyDTO assignment = assignmentService.create(employeePublicId, dto);
        return ResponseEntity
                .created(buildLocationUri(assignment.publicId()))
                .body(assignment);
    }

    @PreAuthorize("hasAuthority('ASSIGNMENT_CANCEL')")
    @PostMapping("/{assignmentPublicId}/cancel")
    public ResponseEntity<Void> cancelAssignment(
            @PathVariable UUID employeePublicId,
            @PathVariable UUID assignmentPublicId
    ) {
        assignmentService.cancel(employeePublicId, assignmentPublicId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ASSIGNMENT_TERMINATE')")
    @PostMapping("/{assignmentPublicId}/terminate")
    public ResponseEntity<Void> terminateAssignment(
            @PathVariable UUID employeePublicId,
            @PathVariable UUID assignmentPublicId
    ) {
        assignmentService.terminate(employeePublicId, assignmentPublicId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ASSIGNMENT_READ')")
    @GetMapping
    public List<EmployeeAssignmentReadOnlyDTO> getAllAssignments(
            @PathVariable UUID employeePublicId,
            @RequestParam(defaultValue = "ACTIVE") EmployeeAssignmentView view) {

        return assignmentService.getAllAssignments(employeePublicId, view);
    }

    @PreAuthorize("hasAuthority('ASSIGNMENT_READ')")
    @GetMapping("/{assignmentPublicId}")
    public EmployeeAssignmentReadOnlyDTO getAssignmentByPublicId(
            @PathVariable UUID employeePublicId,
            @PathVariable UUID assignmentPublicId) {

        return assignmentService.getAssignmentByPublicId(employeePublicId, assignmentPublicId);
    }

    private URI buildLocationUri(UUID assignmentPublicId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{assignmentPublicId}")
                .buildAndExpand(assignmentPublicId)
                .toUri();
    }
}
