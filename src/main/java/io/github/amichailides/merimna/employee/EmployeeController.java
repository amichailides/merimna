package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.common.response.PageResponse;
import io.github.amichailides.merimna.employee.dto.*;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(
        name = "Employees",
        description = "Manage employee profiles, employment details, and lifecycle"
)
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_CREATE')")
    public ResponseEntity<EmployeeDetailsDTO> createEmployee(
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeCreateDTO dto) {

        EmployeeDetailsDTO employee = employeeService.createEmployee(dto);
        return ResponseEntity
                .created(buildLocationUri(employee.publicId()))
                .body(employee);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public ResponseEntity<PageResponse<EmployeeListDTO>> getAllEmployees(
            @Validated(ValidationGroupSequence.class) @ModelAttribute EmployeeSearchDTO criteria,
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

    @GetMapping("/{employeePublicId}")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public ResponseEntity<EmployeeDetailsDTO> getEmployeeByPublicId(
            @PathVariable UUID employeePublicId) {

        EmployeeDetailsDTO result = employeeService.getEmployeeByPublicId(employeePublicId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{employeePublicId}/activity")
    @PreAuthorize("hasAuthority('EMPLOYEE_ACTIVITY_READ')")
    public ResponseEntity<PageResponse<EmployeeActivityDTO>> getEmployeeActivity(
            @PathVariable UUID employeePublicId,
            @ParameterObject @PageableDefault(size = 3, sort = "occurredAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<EmployeeActivityDTO> page = employeeService.getEmployeeActivity(employeePublicId, pageable);

        return ResponseEntity.ok(PageResponse.<EmployeeActivityDTO>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build()
        );
    }

    @PatchMapping("/{employeePublicId}")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    public ResponseEntity<EmployeeDetailsDTO> updateEmployee(
            @PathVariable UUID employeePublicId,
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeUpdateDTO dto) {

        EmployeeDetailsDTO result = employeeService.updateEmployee(employeePublicId, dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{employeePublicId}/terminate")
    @PreAuthorize("hasAuthority('EMPLOYEE_TERMINATE')")
    public ResponseEntity<EmployeeDetailsDTO> terminateEmployee(
            @PathVariable UUID employeePublicId,
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeTerminateDTO dto) {

        EmployeeDetailsDTO result = employeeService.terminate(employeePublicId, dto.terminationDate());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{employeePublicId}/reactivate")
    @PreAuthorize("hasAuthority('EMPLOYEE_REACTIVATE')")
    public ResponseEntity<EmployeeDetailsDTO> reactivateEmployee(
            @PathVariable UUID employeePublicId) {

        EmployeeDetailsDTO result = employeeService.reactivate(employeePublicId);
        return ResponseEntity.ok(result);
    }

    private URI buildLocationUri(UUID employeePublicId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{publicId}")
                .buildAndExpand(employeePublicId)
                .toUri();
    }
}
