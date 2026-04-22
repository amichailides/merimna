package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.legalrepresentative.LegalRepresentativeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Legal Representatives", description = "Operations related to legal representatives")
@RestController
@RequestMapping("/beneficiaries/{beneficiaryPublicId}/legal-representatives")
@RequiredArgsConstructor
public class BeneficiaryLegalRepresentativeController {
    private final LegalRepresentativeService legalRepresentativeService;

    @PostMapping("/{legalRepresentativeId}")
    public ResponseEntity<Void> assignLegalRepresentative(
            @PathVariable String beneficiaryPublicId,
            @PathVariable Long legalRepresentativeId) {

        legalRepresentativeService.assignToBeneficiary(beneficiaryPublicId, legalRepresentativeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{legalRepresentativeId}")
    public ResponseEntity<Void> unassignLegalRepresentative(
            @PathVariable String beneficiaryPublicId,
            @PathVariable Long legalRepresentativeId) {

        legalRepresentativeService.unassignLegalRepresentative(beneficiaryPublicId, legalRepresentativeId);
        return ResponseEntity.noContent().build();
    }

}
