package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
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
    @Size(min = 2, max = 50, message = "{validation.beneficiary.q.size}")
    private String q; // query term (global search)

    @ValidAmka
    private String amka;

    private Boolean includeInactive = false;

    private HouseUnit houseUnit;
}

