package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitReadOnlyDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitUpdateDTO;
import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.constraints.Pattern;
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
    public List<HouseUnitReadOnlyDTO> getAllHouseUnits() {

        return houseUnitService.getAllHouseUnits();
    }

    @PostMapping
    public ResponseEntity<HouseUnitReadOnlyDTO> createHouseUnit(
            @Validated(ValidationGroupSequence.class) @RequestBody HouseUnitCreateDTO dto) {

        HouseUnitReadOnlyDTO houseUnit = houseUnitService.createHouseUnit(dto);
        return ResponseEntity
                .created(buildLocationUri(houseUnit.code()))
                .body(houseUnit);
    }

    @PatchMapping("/{houseUnitCode}")
    public ResponseEntity<HouseUnitReadOnlyDTO> updateHouseUnit(
            @PathVariable
            @Pattern(regexp = ValidationPatterns.HOUSE_UNIT_CODE, message = "{houseUnit.code.invalid}")
            String houseUnitCode,
            @Validated(ValidationGroupSequence.class) @RequestBody HouseUnitUpdateDTO dto) {

        HouseUnitReadOnlyDTO updated = houseUnitService.updateHouseUnit(houseUnitCode, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{code}")
    public HouseUnitReadOnlyDTO getHouseUnitByCode(
            @PathVariable
            @Pattern(regexp = ValidationPatterns.HOUSE_UNIT_CODE, message = "{houseUnit.code.invalid}")
            String code) {

        return houseUnitService.getHouseUnitByCode(code);
    }

    private URI buildLocationUri(String code) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(code)
                .toUri();
    }
}
