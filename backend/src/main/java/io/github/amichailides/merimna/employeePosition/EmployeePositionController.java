package io.github.amichailides.merimna.employeePosition;

import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionCreateDTO;
import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionReadOnlyDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee-positions")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Employee Positions",
        description = "Manage employee positions and their assigned permissions"
)
public class EmployeePositionController {

    private final EmployeePositionService positionService;

    @PostMapping
    public ResponseEntity<EmployeePositionReadOnlyDTO> create(
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeePositionCreateDTO dto) {

        EmployeePositionReadOnlyDTO result = positionService.create(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public List<EmployeePositionReadOnlyDTO> getPositions() {
        return positionService.getPositions();
    }
}
