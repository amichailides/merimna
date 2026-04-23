package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitReadOnlyDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitUpdateDTO;
import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.constraints.Pattern;
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
@RequestMapping("house-units")
@Validated
@RequiredArgsConstructor
public class HouseUnitController {

    private final HouseUnitService houseUnitService;

    @PreAuthorize("hasAuthority('HOUSE_UNIT_READ')")
    @GetMapping
    public List<HouseUnitReadOnlyDTO> getAllHouseUnits() {

        return houseUnitService.getAllHouseUnits();
    }

    @PreAuthorize("hasAuthority('HOUSE_UNIT_CREATE')")
    @PostMapping
    public ResponseEntity<HouseUnitReadOnlyDTO> createHouseUnit(
            @Validated(ValidationGroupSequence.class) @RequestBody HouseUnitCreateDTO dto) {

        HouseUnitReadOnlyDTO houseUnit = houseUnitService.createHouseUnit(dto);
        return ResponseEntity
                .created(buildLocationUri(houseUnit.publicId().toString()))
                .body(houseUnit);
    }

    @PreAuthorize("hasAuthority('HOUSE_UNIT_UPDATE')")
    @PatchMapping("/{publicId}")
    public ResponseEntity<HouseUnitReadOnlyDTO> updateHouseUnit(
            @PathVariable UUID publicId,
            @Validated(ValidationGroupSequence.class) @RequestBody HouseUnitUpdateDTO dto) {

        HouseUnitReadOnlyDTO updated = houseUnitService.updateHouseUnit(publicId, dto);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAuthority('HOUSE_UNIT_READ')")
    @GetMapping("/{publicId}")
    public HouseUnitReadOnlyDTO getHouseUnit(
            @PathVariable UUID publicId) {

        return houseUnitService.getHouseUnit(publicId);
    }

    private URI buildLocationUri(String publicId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(publicId)
                .toUri();
    }
}
