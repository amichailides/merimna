package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.legalrepresentative.LegalRepresentativeService;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.github.amichailides.merimna.validation.annotations.ValidUUID;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Legal Representatives", description = "Operations related to legal representatives")
@RestController
@RequestMapping("/beneficiaries/{beneficiaryPublicId}/legal-representatives")
@RequiredArgsConstructor
@Validated(ValidationGroupSequence.class)
public class BeneficiaryLegalRepresentativeController {
    private final LegalRepresentativeService legalRepresentativeService;

    @PostMapping("/{legalRepresentativeId}")
    public ResponseEntity<Void> assignLegalRepresentative(
            @PathVariable
            @NotBlank(message = "{beneficiary.publicId.required}", groups = FirstOrder.class)
            @ValidUUID(message = "{beneficiary.publicId.invalid}", groups = SecondOrder.class)
            String beneficiaryPublicId,

            @PathVariable Long legalRepresentativeId) {

        legalRepresentativeService.assignToBeneficiary(UUID.fromString(beneficiaryPublicId), legalRepresentativeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{legalRepresentativeId}")
    public ResponseEntity<Void> unassignLegalRepresentative(
            @PathVariable
            @NotBlank(message = "{beneficiary.publicId.required}", groups = FirstOrder.class)
            @ValidUUID(message = "{beneficiary.publicId.invalid}", groups = SecondOrder.class)
            String beneficiaryPublicId,

            @PathVariable Long legalRepresentativeId) {

        legalRepresentativeService.unassignLegalRepresentative(UUID.fromString(beneficiaryPublicId), legalRepresentativeId);
        return ResponseEntity.noContent().build();
    }
}