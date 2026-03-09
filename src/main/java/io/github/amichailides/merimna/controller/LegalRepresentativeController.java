package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.service.LegalRepresentativeService;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/beneficiaries/{beneficiaryId}/legal-representative")
@RequiredArgsConstructor
public class LegalRepresentativeController {
    private final LegalRepresentativeService legalRepresentativeService;

    @PostMapping
    public ResponseEntity<LegalRepresentativeReadOnlyDTO> addLegalRepresentative (
            @PathVariable Long beneficiaryId,
            @Validated(ValidationGroupSequence.class) @RequestBody LegalRepresentativeDTO dto
    ) {
        LegalRepresentativeReadOnlyDTO  legalRepresentativeReadOnly = legalRepresentativeService.addLegalRepresentative(beneficiaryId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(legalRepresentativeReadOnly);
    }

}
