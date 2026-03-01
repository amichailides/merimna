package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.service.AllergiesService;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        // TODO: Add Location header for created allergy (REST best practice)
        return ResponseEntity
                .status(HttpStatus.CREATED)
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
}
