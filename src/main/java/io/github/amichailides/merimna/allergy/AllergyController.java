package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@Tag(name = "Allergies", description = "Operations related to allergies")
@RestController
@Validated
@RequestMapping("/beneficiaries/{beneficiaryId}/allergies")
@RequiredArgsConstructor
public class AllergyController {
    private final AllergyService allergyService;

    @PostMapping
    public ResponseEntity<AllergyReadOnlyDTO> addAllergy(
            @PathVariable Long beneficiaryId,
            @Validated(ValidationGroupSequence.class) @RequestBody AllergyCreateDTO dto) {

        AllergyReadOnlyDTO allergy = allergyService.addAllergy(beneficiaryId, dto);

        return ResponseEntity
                .created(buildLocationUri(allergy.id()))
                .body(allergy);
    }

    @PatchMapping("/{allergyId}")
    public ResponseEntity<AllergyReadOnlyDTO> updateAllergy(
            @PathVariable Long beneficiaryId,
            @PathVariable Long allergyId,
            @Validated(ValidationGroupSequence.class) @RequestBody AllergyUpdateDTO dto) {

        AllergyReadOnlyDTO allergy = allergyService.updateAllergy(beneficiaryId, allergyId, dto);
        return ResponseEntity.ok(allergy);
    }

    @DeleteMapping("/{allergyId}")
    public ResponseEntity<Void> deleteAllergy(
            @PathVariable Long beneficiaryId,
            @PathVariable Long allergyId) {

        allergyService.deleteAllergy(beneficiaryId, allergyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AllergyReadOnlyDTO>> getAllergies (@PathVariable Long beneficiaryId) {

        List<AllergyReadOnlyDTO> allergies = allergyService.getAllergiesByBeneficiary(beneficiaryId);

        return ResponseEntity.ok(allergies);
    }

    @GetMapping("/{allergyId}")
    public ResponseEntity<AllergyReadOnlyDTO> getAllergy (
            @PathVariable Long beneficiaryId,
            @PathVariable Long allergyId) {

        AllergyReadOnlyDTO allergy = allergyService.getAllergy(beneficiaryId, allergyId);
        return ResponseEntity.ok(allergy);

    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
