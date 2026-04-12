package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.assignment.EmployeeAssignmentService;
import io.github.amichailides.merimna.assignment.EmployeeAssignmentView;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.common.response.PageResponse;
import io.github.amichailides.merimna.employee.dto.*;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees")
@Validated
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeAssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<EmployeeDetailsDTO> createEmployee(
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeCreateDTO dto) {

        EmployeeDetailsDTO employee = employeeService.createEmployee(dto);
        return ResponseEntity
                .created(buildLocationUri(employee.id()))
                .body(employee);
    }

    @GetMapping
    public ResponseEntity<PageResponse<EmployeeListDTO>> getAllEmployees(
            @Valid @ModelAttribute EmployeeSearchDTO criteria,
            @ParameterObject @PageableDefault(size = 5, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<EmployeeListDTO> page = employeeService.getAllEmployees(criteria, pageable);

        return ResponseEntity.ok(PageResponse.<EmployeeListDTO>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDetailsDTO> getEmployeeById(
            @PathVariable @Positive(message = "{employee.id.positive}")
            Long id) {
        EmployeeDetailsDTO result = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeDetailsDTO> updateEmployee(
            @PathVariable @Positive(message = "{employee.id.positive}")
            Long id,
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeUpdateDTO dto) {

        EmployeeDetailsDTO result = employeeService.updateEmployee(id, dto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/terminate")
    public ResponseEntity<EmployeeDetailsDTO> terminateEmployee(
            @PathVariable
            @Positive (message = "{employee.id.positive}")
            Long id,
            @RequestBody @Valid EmployeeTerminateDTO dto) {

        EmployeeDetailsDTO result = employeeService.terminate(id, dto.terminationDate());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/reactivate")
    public ResponseEntity<EmployeeDetailsDTO> reactivateEmployee(
            @PathVariable
            @Positive(message = "{employee.id.positive}")
            Long id) {

        EmployeeDetailsDTO result = employeeService.reactivate(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/assignments")
    public List<EmployeeAssignmentReadOnlyDTO> getAllAssignments(
            @PathVariable @Positive(message = "{employee.id.positive}") Long id,
            @RequestParam (defaultValue = "ACTIVE")EmployeeAssignmentView view) {

        return assignmentService.getAllAssignments(id, view);
    }

    @GetMapping("/{employeeId}/assignments/{assignmentId}")
    public ResponseEntity<EmployeeAssignmentReadOnlyDTO> getAssignmentById(
            @PathVariable @Positive(message = "{employee.id.positive}") Long employeeId,
            @PathVariable @Positive(message = "{assignment.id.positive}") Long assignmentId) {

        EmployeeAssignmentReadOnlyDTO assignment = assignmentService.getAssignmentById(employeeId, assignmentId);
        return ResponseEntity.ok(assignment);
    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
