package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementCreateDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/placements")
@Validated
@RequiredArgsConstructor
public class EmployeePlacementController {

    private final EmployeePlacementService placementService;

    @PostMapping
    public ResponseEntity<EmployeePlacementReadOnlyDTO> create(
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeePlacementCreateDTO dto) {

        EmployeePlacementReadOnlyDTO placement = placementService.create(dto);
        return ResponseEntity
                .created(buildLocationUri(placement.publicId()))
                .body(placement);
    }

    @GetMapping("")


    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
