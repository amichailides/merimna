package io.github.amichailides.merimna.employeePosition;

import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionCreateDTO;
import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionReadOnlyDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee-positions")
@Validated
@RequiredArgsConstructor
public class EmployeePositionController {

    private final EmployeePositionService positionService;

    @PostMapping
    public ResponseEntity<EmployeePositionReadOnlyDTO> create(
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeePositionCreateDTO dto) {

        EmployeePositionReadOnlyDTO result = positionService.create(dto);
        return ResponseEntity.ok(result);
    }
}
