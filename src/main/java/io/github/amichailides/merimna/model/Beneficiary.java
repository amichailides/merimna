package io.github.amichailides.merimna.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;

    @NonNull
    @Column(nullable = false, unique = true, length = 11)
    private String amka;

    @NonNull
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HouseUnit houseUnit;

    /*
     * Logic: Ενώ η κλάση Address απαιτεί πλήρη στοιχεία, εδώ η διεύθυνση
     * είναι προαιρετική (nullable = true) γιατί ο ωφελούμενος μπορεί να μην έχει
     * πλέον μόνιμη κατοικία ή οικογένεια.
     * Αν όμως οριστεί το αντικείμενο, πρέπει να ακολουθεί το NonNull συμβόλαιο της Address.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "perm_street", nullable = true)),
            @AttributeOverride(name = "streetNumber", column = @Column (name = "perm_number", nullable = true)),
            @AttributeOverride(name = "city", column = @Column(name = "perm_city", nullable = true)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "perm_zip", nullable = true))
    })
    private Address permanentAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "emergency_first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "emergency_last_name")),
            @AttributeOverride(name = "relationship", column = @Column(name = "emergency_relationship"))
    })
    private EmergencyContact emergencyContact;

    @ElementCollection
    @CollectionTable(name = "beneficiary_medication", joinColumns = @JoinColumn(name = "beneficiary_id"))
    @OrderColumn(name = "med_order")
    @Builder.Default
    private List<Medication> medicalTreatment = new ArrayList<>();

    @OneToMany(mappedBy = "beneficiary", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private Set<Allergy> allergies = new HashSet<>();

    public void addAllergy(@NonNull Allergy allergy) {
        this.allergies.add(allergy);
        allergy.assignToBeneficiary(this);
    }

    public void removeAllergy(@NonNull Allergy allergy) {
        this.allergies.remove(allergy);
        allergy.clearBeneficiary();
    }

    public Set<Allergy> getAllergies() {
        return java.util.Collections.unmodifiableSet(allergies);
    }
}
