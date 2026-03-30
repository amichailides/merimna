package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.employee.dto.EmployeeCreateDTO;
import io.github.amichailides.merimna.employee.dto.EmployeeDetailsDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/employees")
@Validated
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDetailsDTO> createEmployee(
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeeCreateDTO dto) {

        EmployeeDetailsDTO employee = employeeService.createEmployee(dto);
        return ResponseEntity
                .created(buildLocationUri(employee.id()))
                .body(employee);
    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
