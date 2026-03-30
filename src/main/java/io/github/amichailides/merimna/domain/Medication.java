package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.medication.exception.MedicationAlreadyAssignedException;
import jakarta.persistence.*;
import lombok.*;

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
    @EqualsAndHashCode.Include
    Long id;

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
     * Logic: Οι ώρες αποθηκεύονται ως String (π.χ. "08:00, 20:00") για να είναι
     * συμβατές με το database mapping των ElementCollections.
     * TODO: Στο μέλλον μπορεί να μετατραπεί σε List<LocalTime> μέσω AttributeConverter
     * για την υποστήριξη push notifications στο mobile app.
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
            throw new MedicationAlreadyAssignedException();
        }
        this.beneficiary = beneficiary;
    }

    void clearBeneficiary() {
        this.beneficiary = null;
    }
}
