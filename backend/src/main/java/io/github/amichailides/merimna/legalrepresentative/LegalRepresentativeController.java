package io.github.amichailides.merimna.legalrepresentative;

import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeCreateDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeUpdateDTO;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Legal Representatives", description = "Operations related to legal representatives")
@Validated
@RestController
@RequestMapping("/legal-representatives")
@RequiredArgsConstructor
public class LegalRepresentativeController {

    private final LegalRepresentativeService legalRepresentativeService;

    @PreAuthorize("hasAuthority('BENEFICIARY_CREATE')")
    @PostMapping
    public ResponseEntity<LegalRepresentativeReadOnlyDTO> createLegalRepresentative(
            @Validated(ValidationGroupSequence.class) @RequestBody LegalRepresentativeCreateDTO dto) {

        LegalRepresentativeReadOnlyDTO legal = legalRepresentativeService.createLegalRepresentative(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(legal);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @PatchMapping("/{legalRepresentativeId}")
    public ResponseEntity<LegalRepresentativeReadOnlyDTO> updateLegalRepresentative(
            @PathVariable
            @Positive(message = "{legalRepresentative.id.positive}")
            Long legalRepresentativeId,
            @Validated(ValidationGroupSequence.class) @RequestBody LegalRepresentativeUpdateDTO dto) {

        LegalRepresentativeReadOnlyDTO legal = legalRepresentativeService.updateLegalRepresentative(legalRepresentativeId, dto);
        return ResponseEntity.ok(legal);
    }

    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping("/{legalRepresentativeId}")
    public LegalRepresentativeReadOnlyDTO getLegalRepresentativeById(
            @PathVariable
            @Positive(message = "{legalRepresentative.id.positive}")
            Long legalRepresentativeId) {

        return legalRepresentativeService.getLegalRepresentativeById(legalRepresentativeId);
    }
}
