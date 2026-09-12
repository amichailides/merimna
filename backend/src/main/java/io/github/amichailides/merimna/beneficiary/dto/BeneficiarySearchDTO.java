package io.github.amichailides.merimna.beneficiary.dto;

import io.github.amichailides.merimna.validation.annotations.OptionalNotBlank;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * Κριτήρια αναζήτησης ωφελούμενων.
 *
 * <p>Αν δοθεί {@code amka}, το {@code q} αγνοείται.</p>
 *
 * <ul>
 *   <li>{@code q} — fuzzy αναζήτηση σε firstName, lastName, amka (partial)</li>
 *   <li>{@code amka} — exact match, για άμεση αναζήτηση με γνωστό AMKA</li>
 * </ul>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiarySearchDTO {

    @Schema(description = "Global search term (firstName, lastName, AMKA partial match). Ignored if amka is provided.", example = "Παπαδόπουλος")
    @OptionalNotBlank(message = "{beneficiary.searchTerm.blank}", groups = FirstOrder.class)
    @Size(min = 2, max = 50, message = "{beneficiary.searchTerm.size}", groups = SecondOrder.class)
    private String q;

    @Schema(description = "Exact AMKA lookup. When present, q is ignored.", example = "12345678901")
    @ValidAmka(groups = SecondOrder.class)
    private String amka;

    // TODO(#7): Evaluate replacing includeInactive with tri-state active filter
    @Schema(description = "Include inactive beneficiaries in results. Defaults to false.", example = "false")
    private boolean includeInactive;

    @Schema(
            description = "Filter by house unit public identifier, usually selected from a UI dropdown.",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID houseUnitPublicId;
}
