package io.github.amichailides.merimna.employee;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/employees")
@Validated
@RequiredArgsConstructor
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

    @GetMapping("/{publicId}")
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public ResponseEntity<EmployeeDetailsDTO> getEmployeeByPublicId(
            @PathVariable String publicId) {
        EmployeeDetailsDTO result = employeeService.getEmployeeByPublicId(publicId);

        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{publicId}")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    public ResponseEntity<EmployeeDetailsDTO> updateEmployee(
            @PathVariable String publicId,
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeUpdateDTO dto) {

        EmployeeDetailsDTO result = employeeService.updateEmployee(publicId, dto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{publicId}/terminate")
    @PreAuthorize("hasAuthority('EMPLOYEE_TERMINATE')")
    public ResponseEntity<EmployeeDetailsDTO> terminateEmployee(
            @PathVariable String publicId,
            @RequestBody @Valid EmployeeTerminateDTO dto) {

        EmployeeDetailsDTO result = employeeService.terminate(publicId, dto.terminationDate());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{publicId}/reactivate")
    @PreAuthorize("hasAuthority('EMPLOYEE_REACTIVATE')")
    public ResponseEntity<EmployeeDetailsDTO> reactivateEmployee(
            @PathVariable String publicId) {

        EmployeeDetailsDTO result = employeeService.reactivate(publicId);

        return ResponseEntity.ok(result);
    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
