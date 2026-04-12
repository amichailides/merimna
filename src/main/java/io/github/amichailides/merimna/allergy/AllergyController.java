package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
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
    public ResponseEntity<AllergyReadOnlyDTO> createAllergy(
            @PathVariable @Positive(message = "{beneficiary.id.positive}") Long beneficiaryId,
            @Validated(ValidationGroupSequence.class) @RequestBody AllergyCreateDTO dto) {

        AllergyReadOnlyDTO allergy = allergyService.createAllergy(beneficiaryId, dto);

        return ResponseEntity
                .created(buildLocationUri(allergy.id()))
                .body(allergy);
    }

    @PatchMapping("/{allergyId}")
    public ResponseEntity<AllergyReadOnlyDTO> updateAllergy(
            @PathVariable @Positive(message = "{beneficiary.id.positive}") Long beneficiaryId,
            @PathVariable @Positive(message = "{allergy.id.positive}") Long allergyId,
            @Validated(ValidationGroupSequence.class) @RequestBody AllergyUpdateDTO dto) {

        AllergyReadOnlyDTO allergy = allergyService.updateAllergy(beneficiaryId, allergyId, dto);
        return ResponseEntity.ok(allergy);
    }

    @DeleteMapping("/{allergyId}")
    public ResponseEntity<Void> deleteAllergy(
            @PathVariable @Positive(message = "{beneficiary.id.positive}") Long beneficiaryId,
            @PathVariable @Positive(message = "{allergy.id.positive}") Long allergyId) {

        allergyService.deleteAllergy(beneficiaryId, allergyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<AllergyReadOnlyDTO> getAllergiesByBeneficiary(
            @PathVariable @Positive(message = "{beneficiary.id.positive}") Long beneficiaryId) {

        return allergyService.getAllergiesByBeneficiary(beneficiaryId);
    }

    @GetMapping("/{allergyId}")
    public AllergyReadOnlyDTO getAllergyById(
            @PathVariable @Positive(message = "{beneficiary.id.positive}") Long beneficiaryId,
            @PathVariable @Positive(message = "{allergy.id.positive}") Long allergyId) {

        return allergyService.getAllergyById(beneficiaryId, allergyId);
    }

    private URI buildLocationUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
