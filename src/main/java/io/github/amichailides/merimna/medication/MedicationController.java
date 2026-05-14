package io.github.amichailides.merimna.medication;

import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Medications", description = "Operations related to medications")
@RestController
@Validated
@RequestMapping("/beneficiaries/{beneficiaryPublicId}/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;

    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @PostMapping
    public ResponseEntity<MedicationReadOnlyDTO> addMedication(
            @PathVariable UUID beneficiaryPublicId,
            @Validated(ValidationGroupSequence.class) @RequestBody MedicationCreateDTO dto) {

        MedicationReadOnlyDTO medication = medicationService.addMedication(beneficiaryPublicId, dto);

        // TODO: When the frontend is integrated, expose the "Location" header in the CORS configuration

        return ResponseEntity
                .created(buildLocationUri(medication.publicId()))
                .body(medication);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping
    public List<MedicationReadOnlyDTO> getMedications(
            @PathVariable UUID beneficiaryPublicId) {

        return medicationService.getMedicationsByBeneficiary(beneficiaryPublicId);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping("/{medicationPublicId}")
    public MedicationReadOnlyDTO getMedicationByPublicId(
            @PathVariable UUID beneficiaryPublicId,
            @PathVariable UUID medicationPublicId) {

        return medicationService.getMedicationByPublicId(beneficiaryPublicId, medicationPublicId);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @PatchMapping("/{medicationPublicId}")
    public ResponseEntity<MedicationReadOnlyDTO> updateMedication(
            @PathVariable UUID beneficiaryPublicId,
            @PathVariable UUID medicationPublicId,
            @Validated(ValidationGroupSequence.class) @RequestBody MedicationUpdateDTO dto) {

        MedicationReadOnlyDTO updated = medicationService.updateMedication(
                beneficiaryPublicId,
                medicationPublicId,
                dto
        );

        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @DeleteMapping("/{medicationPublicId}")
    public ResponseEntity<Void> deleteMedication(
            @PathVariable UUID beneficiaryPublicId,
            @PathVariable UUID medicationPublicId) {

        medicationService.deleteMedication(beneficiaryPublicId, medicationPublicId);

        return ResponseEntity.noContent().build();
    }

    private URI buildLocationUri(UUID medicationPublicId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{medicationPublicId}")
                .buildAndExpand(medicationPublicId)
                .toUri();
    }
}
