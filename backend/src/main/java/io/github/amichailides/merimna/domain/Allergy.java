package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.allergy.exception.AllergyAlreadyAssignedException;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
import static io.github.amichailides.merimna.validation.TextNormalizer.normalize;

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
    @Setter(AccessLevel.NONE)
    private Long id;

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    @Builder.Default
    private UUID publicId = UUID.randomUUID();

    @NonNull
    @Column(nullable = false)
    private String substance;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AllergySeverity severity;

    @NonNull
    @Column(nullable = false)
    private String reaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Beneficiary beneficiary;

    public void assignToBeneficiary(@NonNull Beneficiary beneficiary) {
        if (this.beneficiary != null && !this.beneficiary.equals(beneficiary)) {
            throw new AllergyAlreadyAssignedException(
                    this.beneficiary.getPublicId(),
                    beneficiary.getPublicId()
            );
        }
        this.beneficiary = beneficiary;
    }

    public void clearBeneficiary() {
        this.beneficiary = null;
    }

    public boolean belongsTo(UUID beneficiaryPublicId) {
        return beneficiary != null
                && beneficiaryPublicId != null
                && beneficiaryPublicId.equals(this.beneficiary.getPublicId());
    }

    @PrePersist
    @PreUpdate
    private void normalizeFields() {
        this.substance = normalize(this.substance);
        this.reaction = normalize(this.reaction);
    }
}
