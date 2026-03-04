package io.github.amichailides.merimna.controller;


import io.github.amichailides.merimna.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.dto.MedicationUpdateDTO;
import io.github.amichailides.merimna.service.MedicationService;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// TODO: Standardize API responses after feature completion
// - Apply pure REST style to all endpoints (remove ApiResponse wrapper)
// - Ensure all endpoints follow consistent pattern
// - Use Location header for created resources
// - Success messages in body removed; frontend handles notifications
@RestController
@Validated
@RequestMapping("/beneficiaries/{beneficiaryId}/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<MedicationReadOnlyDTO> addMedication(
            @PathVariable Long beneficiaryId,
            @Validated(ValidationGroupSequence.class) @RequestBody MedicationCreateDTO dto) {

        MedicationReadOnlyDTO medication = medicationService.addMedication(beneficiaryId, dto);

        // TODO: Όταν συνδεθεί το Front-end, πρέπει να γίνει expose το "Location" header στο CORS config


        return ResponseEntity
                .created(buildLocationUri(medication.id()))
                .body(medication);
    }

    @GetMapping
    public ResponseEntity<List<MedicationReadOnlyDTO>> getMedications(@PathVariable Long beneficiaryId) {

        List<MedicationReadOnlyDTO> medications = medicationService.getMedicationsByBeneficiary(beneficiaryId);

        return ResponseEntity.ok(medications);
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

    /**
     * Helper για να τραβάμε τα μηνύματα επιτυχίας.
     */
    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
