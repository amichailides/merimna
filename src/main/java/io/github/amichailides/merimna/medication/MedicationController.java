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
@RequestMapping("/beneficiaries/{beneficiaryPublicId}/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;

    @PreAuthorize("hasAuthority('BENEFICIARY_CREATE')")
    @PostMapping
    public ResponseEntity<MedicationReadOnlyDTO> addMedication(
            @PathVariable String beneficiaryPublicId,
            @Validated(ValidationGroupSequence.class) @RequestBody MedicationCreateDTO dto) {

        MedicationReadOnlyDTO medication = medicationService.addMedication(beneficiaryPublicId, dto);

        // TODO: When the frontend is integrated, expose the "Location" header in the CORS configuration

        return ResponseEntity
                .created(buildLocationUri(medication.id()))
                .body(medication);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping
    public List<MedicationReadOnlyDTO> getMedications(@PathVariable String beneficiaryPublicId) {

        return medicationService.getMedicationsByBeneficiary(beneficiaryPublicId);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping("/{medicationId}")
    public MedicationReadOnlyDTO getMedication(
            @PathVariable String beneficiaryPublicId,
            @PathVariable @Positive(message = "{medication.id.positive}") Long medicationId) {

        return medicationService.getMedication(beneficiaryPublicId, medicationId);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @PatchMapping("/{medicationId}")
    public ResponseEntity<MedicationReadOnlyDTO> updateMedication(
            @PathVariable String beneficiaryPublicId,
            @PathVariable Long medicationId,
            @Validated(ValidationGroupSequence.class) @RequestBody MedicationUpdateDTO dto) {

        MedicationReadOnlyDTO updated = medicationService.updateMedication(beneficiaryPublicId, medicationId, dto);

        return ResponseEntity.ok(updated);

    }

    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @DeleteMapping("/{medicationId}")
    public ResponseEntity<Void> deleteMedication (
            @PathVariable String beneficiaryPublicId,
            @PathVariable Long medicationId) {

        medicationService.deleteMedication(beneficiaryPublicId, medicationId);

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
