package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @Size(min = 2, max = 50, message = "{validation.beneficiary.q.size}")
    private String q;

    @Schema(description = "Exact AMKA lookup. When present, q is ignored.", example = "12345678901")
    @ValidAmka
    private String amka;

    // TODO(#7): Evaluate replacing includeInactive with tri-state active filter
    @Schema(description = "Include inactive beneficiaries in results. Defaults to false.", example = "false")
    private boolean includeInactive;  // defaults to false

    @Schema(description = "Filter by house unit", example = "UNIT_A")
    private HouseUnit houseUnit;
}
