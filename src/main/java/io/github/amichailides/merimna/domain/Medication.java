package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.medication.exception.MedicationAlreadyAssignedException;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "medications")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    @Builder.Default
    private UUID publicId = UUID.randomUUID();

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String dosage;

    @NonNull
    @Column(nullable = false)
    private String frequency;

    /*
     * Administration times are stored as a comma-separated String
     * to keep the current ElementCollection database mapping simple.
     *
     * TODO: Consider replacing this with List<LocalTime> and an AttributeConverter
     * if medication reminders or mobile push notifications require time-aware logic.
     */
    @NonNull
    @Column(nullable = false)
    private String administrationTimes;

    @Column
    private String instructions;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    @Setter(AccessLevel.PACKAGE)
    private Beneficiary beneficiary;

    public void assignToBeneficiary(@NonNull Beneficiary beneficiary) {
        if (this.beneficiary != null && !this.beneficiary.equals(beneficiary)) {
            throw new MedicationAlreadyAssignedException(
                    this.beneficiary.getPublicId(),
                    beneficiary.getPublicId()
            );
        }
        this.beneficiary = beneficiary;
    }

    void clearBeneficiary() {
        this.beneficiary = null;
    }
}
