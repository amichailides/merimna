package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementCreateDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementTerminateDTO;
import io.github.amichailides.merimna.validation.annotations.ValidUUID;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

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

    @GetMapping("/{publicId}")
    public EmployeePlacementReadOnlyDTO getPlacement(
            @PathVariable
            @NotBlank(message = "{placement.publicId.required}")
            @ValidUUID(message = "{placement.publicId.invalid}")
            String publicId) {

        return placementService.getByPublicId(UUID.fromString(publicId));
    }

    @PostMapping("/{publicId}/terminate")
    public ResponseEntity<Void> terminate(
            @PathVariable
            @NotBlank(message = "{placement.publicId.required}")
            @ValidUUID(message = "{placement.publicId.invalid}")
            String publicId,
            @Validated(ValidationGroupSequence.class)
            @RequestBody EmployeePlacementTerminateDTO dto) {

        placementService.terminate(UUID.fromString(publicId), dto);
        return ResponseEntity.noContent().build();
    }


    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
