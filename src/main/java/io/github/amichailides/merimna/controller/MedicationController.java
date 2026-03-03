package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.common.ApiResponse;
import io.github.amichailides.merimna.dto.MedicationCreateDTO;
import io.github.amichailides.merimna.dto.MedicationReadOnlyDTO;
import io.github.amichailides.merimna.service.MedicationService;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Validated
@RequestMapping("/beneficiaries/{beneficiaryId}/medications")
@RequiredArgsConstructor
public class MedicationController {
    private final MedicationService medicationService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicationReadOnlyDTO>> addMedication(
            @PathVariable Long beneficiaryId,
            @Validated(ValidationGroupSequence.class) @RequestBody MedicationCreateDTO dto) {

        MedicationReadOnlyDTO medication = medicationService.addMedication(beneficiaryId, dto);

        // TODO: Όταν συνδεθεί το Front-end, πρέπει να γίνει expose το "Location" header στο CORS config
        // location header
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(medication.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(ApiResponse.success(
                        medication,
                        getMessage("medication.create.success"),
                        HttpStatus.CREATED.value()
                ));
    }

    /**
     * Helper για να τραβάμε τα μηνύματα επιτυχίας.
     */
    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
