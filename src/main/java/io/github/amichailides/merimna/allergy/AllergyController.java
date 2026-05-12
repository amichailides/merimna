package io.github.amichailides.merimna.allergy;

import io.github.amichailides.merimna.allergy.dto.AllergyCreateDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyUpdateDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Allergies", description = "Operations related to allergies")
@RestController
@Validated
@RequestMapping("/beneficiaries/{beneficiaryPublicId}/allergies")
@RequiredArgsConstructor
public class AllergyController {
    private final AllergyService allergyService;

    @PreAuthorize("hasAuthority('BENEFICIARY_CREATE')")
    @PostMapping
    public ResponseEntity<AllergyReadOnlyDTO> createAllergy(
            @PathVariable UUID beneficiaryPublicId,
            @Validated(ValidationGroupSequence.class) @RequestBody AllergyCreateDTO dto) {

        AllergyReadOnlyDTO allergy = allergyService.createAllergy(beneficiaryPublicId, dto);

        return ResponseEntity
                .created(buildLocationUri(allergy.id()))
                .body(allergy);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @PatchMapping("/{allergyId}")
    public ResponseEntity<AllergyReadOnlyDTO> updateAllergy(
            @PathVariable UUID beneficiaryPublicId,
            @PathVariable @Positive(message = "{allergy.id.positive}") Long allergyId,
            @Validated(ValidationGroupSequence.class) @RequestBody AllergyUpdateDTO dto) {

        AllergyReadOnlyDTO allergy = allergyService.updateAllergy(beneficiaryPublicId, allergyId, dto);
        return ResponseEntity.ok(allergy);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @DeleteMapping("/{allergyId}")
    public ResponseEntity<Void> deleteAllergy(
            @PathVariable UUID beneficiaryPublicId,
            @PathVariable @Positive(message = "{allergy.id.positive}") Long allergyId) {

        allergyService.deleteAllergy(beneficiaryPublicId, allergyId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping
    public List<AllergyReadOnlyDTO> getAllergiesByBeneficiary(
            @PathVariable UUID beneficiaryPublicId) {

        return allergyService.getAllergiesByBeneficiary(beneficiaryPublicId);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping("/{allergyId}")
    public AllergyReadOnlyDTO getAllergyById(
            @PathVariable UUID beneficiaryPublicId,
            @PathVariable @Positive(message = "{allergy.id.positive}") Long allergyId) {

        return allergyService.getAllergyById(beneficiaryPublicId, allergyId);
    }

    private URI buildLocationUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
