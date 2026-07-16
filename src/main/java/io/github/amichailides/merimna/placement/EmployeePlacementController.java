package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.common.response.PageResponse;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementCreateDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementSearchDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementTerminateDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/placements")
@RequiredArgsConstructor
@Tag(
        name = "Employee Placements",
        description = "Manage temporary employee placements across house units"
)
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

    @GetMapping("/{placementPublicId}")
    public EmployeePlacementReadOnlyDTO getPlacement(
            @PathVariable UUID placementPublicId) {

        return placementService.getByPublicId(placementPublicId);
    }

    @PostMapping("/{placementPublicId}/terminate")
    public ResponseEntity<Void> terminate(
            @PathVariable UUID placementPublicId,
            @Validated(ValidationGroupSequence.class) @RequestBody EmployeePlacementTerminateDTO dto) {

        placementService.terminate(placementPublicId, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<EmployeePlacementReadOnlyDTO>> getAllPlacements(
            @Validated(ValidationGroupSequence.class) @ModelAttribute EmployeePlacementSearchDTO criteria,
            Pageable pageable) {

        Page<EmployeePlacementReadOnlyDTO> page = placementService.getAllPlacements(criteria, pageable);

        return ResponseEntity.ok(PageResponse.<EmployeePlacementReadOnlyDTO>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build()
        );
    }


    private URI buildLocationUri(UUID placementPublicId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{placementPublicId}")
                .buildAndExpand(placementPublicId)
                .toUri();
    }
}
