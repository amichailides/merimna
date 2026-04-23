package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.legalrepresentative.LegalRepresentativeService;
import io.github.amichailides.merimna.validation.annotations.ValidUUID;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Legal Representatives", description = "Operations related to legal representatives")
@RestController
@RequestMapping("/beneficiaries/{beneficiaryPublicId}/legal-representatives")
@RequiredArgsConstructor
public class BeneficiaryLegalRepresentativeController {
    private final LegalRepresentativeService legalRepresentativeService;

    @PostMapping("/{legalRepresentativeId}")
    public ResponseEntity<Void> assignLegalRepresentative(
            @PathVariable
            @NotBlank(message = "{beneficiary.publicId.required}")
            @ValidUUID(message = "{beneficiary.publicId.invalid}")
            String beneficiaryPublicId,
            @PathVariable Long legalRepresentativeId) {

        legalRepresentativeService.assignToBeneficiary(UUID.fromString(beneficiaryPublicId), legalRepresentativeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{legalRepresentativeId}")
    public ResponseEntity<Void> unassignLegalRepresentative(
            @PathVariable
            @NotBlank(message = "{beneficiary.publicId.required}")
            @ValidUUID(message = "{beneficiary.publicId.invalid}")
            String beneficiaryPublicId,
            @PathVariable Long legalRepresentativeId) {

        legalRepresentativeService.unassignLegalRepresentative(UUID.fromString(beneficiaryPublicId), legalRepresentativeId);
        return ResponseEntity.noContent().build();
    }

}
