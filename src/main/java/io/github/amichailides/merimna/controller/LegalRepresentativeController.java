package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeUpdateDTO;
import io.github.amichailides.merimna.service.LegalRepresentativeService;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/beneficiaries/{beneficiaryId}/legal-representative")
@RequiredArgsConstructor
public class LegalRepresentativeController {
    private final LegalRepresentativeService legalRepresentativeService;

    @PostMapping
    public ResponseEntity<LegalRepresentativeReadOnlyDTO> addLegalRepresentative (
            @PathVariable Long beneficiaryId,
            @Validated(ValidationGroupSequence.class) @RequestBody LegalRepresentativeDTO dto)
    {
        LegalRepresentativeReadOnlyDTO  legalRepresentativeReadOnly = legalRepresentativeService.addLegalRepresentative(beneficiaryId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(legalRepresentativeReadOnly);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeLegalRepresentative(@PathVariable Long beneficiaryId) {
        legalRepresentativeService.removeLegalRepresentative(beneficiaryId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<LegalRepresentativeReadOnlyDTO> updateLegalRepresentative(
            @PathVariable Long beneficiaryId,
            @Validated(ValidationGroupSequence.class) @RequestBody LegalRepresentativeUpdateDTO dto) {

        LegalRepresentativeReadOnlyDTO legal = legalRepresentativeService.updateLegalRepresentative(beneficiaryId, dto);
        return ResponseEntity.ok(legal);
    }

}
