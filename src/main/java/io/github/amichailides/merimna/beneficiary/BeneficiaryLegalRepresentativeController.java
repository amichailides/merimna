package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.legalrepresentative.LegalRepresentativeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Legal Representatives", description = "Operations related to legal representatives")
@RestController
@RequestMapping("/beneficiaries/{beneficiaryId}/legal-representatives")
@RequiredArgsConstructor
public class BeneficiaryLegalRepresentativeController {
    private final LegalRepresentativeService legalRepresentativeService;

    @PostMapping("/{legalRepresentativeId}")
    public ResponseEntity<Void> assignLegalRepresentative(
            @PathVariable Long beneficiaryId,
            @PathVariable Long legalRepresentativeId) {

        legalRepresentativeService.assignLegalRepresentative(beneficiaryId, legalRepresentativeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{legalRepresentativeId}")
    public ResponseEntity<Void> unassignLegalRepresentative(
            @PathVariable Long beneficiaryId,
            @PathVariable Long legalRepresentativeId) {

        legalRepresentativeService.unassignLegalRepresentative(beneficiaryId, legalRepresentativeId);
        return ResponseEntity.noContent().build();
    }

}
