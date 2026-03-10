package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.service.AllergiesService;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;


@RestController
@Validated
@RequestMapping("/beneficiaries/{beneficiaryId}/allergies")
@RequiredArgsConstructor
public class AllergiesController {
    private final AllergiesService allergiesService;

    @PostMapping
    public ResponseEntity<AllergyReadOnlyDTO> addAllergy(
            @PathVariable Long beneficiaryId,
            @Validated(ValidationGroupSequence.class) @RequestBody AllergyCreateDTO dto) {

        AllergyReadOnlyDTO allergy = allergiesService.addAllergy(beneficiaryId, dto);

        return ResponseEntity
                .created(buildLocationUri(allergy.id()))
                .body(allergy);
    }

    @PatchMapping("/{allergyId}")
    public ResponseEntity<AllergyReadOnlyDTO> updateAllergy(
            @PathVariable Long beneficiaryId,
            @PathVariable Long allergyId,
            @Validated(ValidationGroupSequence.class) @RequestBody AllergyUpdateDTO dto) {

        AllergyReadOnlyDTO allergy = allergiesService.updateAllergy(beneficiaryId, allergyId, dto);
        return ResponseEntity.ok(allergy);
    }

    @DeleteMapping("/{allergyId}")
    public ResponseEntity<Void> deleteAllergy(
            @PathVariable Long beneficiaryId,
            @PathVariable Long allergyId) {

        allergiesService.deleteAllergy(beneficiaryId, allergyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AllergyReadOnlyDTO>> getAllergies (@PathVariable Long beneficiaryId) {

        List<AllergyReadOnlyDTO> allergies = allergiesService.getAllergiesByBeneficiary(beneficiaryId);

        return ResponseEntity.ok(allergies);
    }

    @GetMapping("/{allergyId}")
    public ResponseEntity<AllergyReadOnlyDTO> getAllergy (
            @PathVariable Long beneficiaryId,
            @PathVariable Long allergyId) {

        AllergyReadOnlyDTO allergy = allergiesService.getAllergy(beneficiaryId, allergyId);
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
