package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.allergy.exception.AllergyAlreadyAssignedException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "allergies")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Allergy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String substance; // πενικιλη, φιστικια κλπ

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AllergySeverity severity;

    @NonNull
    @Column(nullable = false)
    private String reaction; // εξανθημα, δυσποινα κλπ

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Beneficiary beneficiary;

    public void assignToBeneficiary(@NonNull Beneficiary beneficiary) {
        if (this.beneficiary != null && !this.beneficiary.equals(beneficiary)) {
            throw new AllergyAlreadyAssignedException();
        }
        this.beneficiary = beneficiary;
    }

    public void clearBeneficiary() {
        this.beneficiary = null;
    }
}
