package io.github.amichailides.merimna.medication;


import io.github.amichailides.merimna.medication.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationUpdateDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping()
    public ResponseEntity<List<MedicationReadOnlyDTO>> getMedications(@PathVariable Long beneficiaryId) {

        List<MedicationReadOnlyDTO> medications = medicationService.getMedicationsByBeneficiary(beneficiaryId);

        return ResponseEntity.ok(medications);
    }

    @GetMapping("/{medicationId}")
    public ResponseEntity<MedicationReadOnlyDTO> getMedication(
            @PathVariable Long beneficiaryId,
            @PathVariable Long medicationId) {

        MedicationReadOnlyDTO dto =  medicationService.getMedication(beneficiaryId, medicationId);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{medicationId}")
    public ResponseEntity<MedicationReadOnlyDTO> updateMedication(
            @PathVariable Long beneficiaryId,
            @PathVariable Long medicationId,
            @Validated(ValidationGroupSequence.class) @RequestBody MedicationUpdateDTO dto) {

        MedicationReadOnlyDTO updated = medicationService.updateMedication(beneficiaryId, medicationId, dto);

        return ResponseEntity.ok(updated);

    }

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
