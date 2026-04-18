package io.github.amichailides.merimna.medication;


import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;
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

@Tag(name = "Medications", description = "Operations related to medications")
@RestController
@Validated
@RequestMapping("/beneficiaries/{beneficiaryId}/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;

    @PreAuthorize("hasAuthority('BENEFICIARY_CREATE')")
    @PostMapping
    public ResponseEntity<MedicationReadOnlyDTO> addMedication(
            @PathVariable Long beneficiaryId,
            @Validated(ValidationGroupSequence.class) @RequestBody MedicationCreateDTO dto) {

        MedicationReadOnlyDTO medication = medicationService.addMedication(beneficiaryId, dto);

        // TODO: When the frontend is integrated, expose the "Location" header in the CORS configuration

        return ResponseEntity
                .created(buildLocationUri(medication.id()))
                .body(medication);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping
    public List<MedicationReadOnlyDTO> getMedications(@PathVariable Long beneficiaryId) {

        return medicationService.getMedicationsByBeneficiary(beneficiaryId);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping("/{medicationId}")
    public MedicationReadOnlyDTO getMedication(
            @PathVariable @Positive(message = "{beneficiary.id.positive}") Long beneficiaryId,
            @PathVariable @Positive(message = "{medication.id.positive}") Long medicationId) {

        return medicationService.getMedication(beneficiaryId, medicationId);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @PatchMapping("/{medicationId}")
    public ResponseEntity<MedicationReadOnlyDTO> updateMedication(
            @PathVariable Long beneficiaryId,
            @PathVariable Long medicationId,
            @Validated(ValidationGroupSequence.class) @RequestBody MedicationUpdateDTO dto) {

        MedicationReadOnlyDTO updated = medicationService.updateMedication(beneficiaryId, medicationId, dto);

        return ResponseEntity.ok(updated);

    }

    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @DeleteMapping("/{medicationId}")
    public ResponseEntity<Void> deleteMedication (
            @PathVariable Long beneficiaryId,
            @PathVariable Long medicationId) {

        medicationService.deleteMedication(beneficiaryId, medicationId);

        return ResponseEntity.noContent().build();
    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
