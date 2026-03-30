package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitReadOnlyDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("house-units")
@Validated
@RequiredArgsConstructor
public class HouseUnitController {

    private final HouseUnitService houseUnitService;

    @GetMapping
    public ResponseEntity<List<HouseUnitReadOnlyDTO>> getAll() {
        List<HouseUnitReadOnlyDTO> houseUnits = houseUnitService.findAll();

        return ResponseEntity.ok(houseUnits);
    }

    @PostMapping
    public ResponseEntity<HouseUnitReadOnlyDTO> createHouseUnit(
            @Validated(ValidationGroupSequence.class) @RequestBody HouseUnitCreateDTO dto) {

        HouseUnitReadOnlyDTO houseUnit = houseUnitService.createHouseUnit(dto);
        return ResponseEntity
                .created(buildLocationUri(houseUnit.id()))
                .body(houseUnit);
    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
