package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.dto.LegalRepresentativeUpdateDTO;
import io.github.amichailides.merimna.service.LegalRepresentativeService;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Legal Representatives", description = "Operations related to legal representatives")
@RestController
@RequestMapping("/legal-representatives")
@RequiredArgsConstructor
public class LegalRepresentativeController {

    private final LegalRepresentativeService legalRepresentativeService;

    @PostMapping
    public ResponseEntity<LegalRepresentativeReadOnlyDTO> createLegalRepresentative(
            @Validated(ValidationGroupSequence.class) @RequestBody LegalRepresentativeDTO dto) {

        LegalRepresentativeReadOnlyDTO legal = legalRepresentativeService.createLegalRepresentative(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(legal);
    }

    @PatchMapping("/{legalRepresentativeId}")
    public ResponseEntity<LegalRepresentativeReadOnlyDTO> updateLegalRepresentative(
            @PathVariable Long legalRepresentativeId,
            @Validated(ValidationGroupSequence.class) @RequestBody LegalRepresentativeUpdateDTO dto) {
        LegalRepresentativeReadOnlyDTO legal = legalRepresentativeService.updateLegalRepresentative(legalRepresentativeId, dto);
        return ResponseEntity.ok(legal);
    }
}
