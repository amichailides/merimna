package io.github.amichailides.merimna.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Οντότητα Ωφελούμενου.
 * * <p><b>TODO (Database Strategy):</b>
 * <ul>
 * <li>Μεταφορά του Schema Management (Indexes, Constraints) σε <b>Liquibase changelogs</b>.</li>
 * <li>Υλοποίηση Partial Index στην PostgreSQL για το {@code is_active = true} (Performance optimization).</li>
 * <li>Μετάβαση σε {@code ddl-auto=validate} για Docker-ready deployment.</li>
 * </ul>
 * </p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Table(name = "beneficiaries",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_beneficiary_amka", columnNames = "amka")
        },
        indexes = {
                @Index(name = "idx_beneficiary_lastname", columnList = "last_name")
        })
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

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HouseUnit houseUnit;

    /*
     * Υποχρεωτική διεύθυνση. Αν ο ωφελούμενος δεν έχει οικογενειακή διεύθυνση,
     * καταχωρείται η διεύθυνση της δομής φιλοξενίας.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "perm_street", nullable = false)),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "perm_number", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "perm_city", nullable = false)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "perm_zip", nullable = false))
    })
    private Address permanentAddress;

    /*
     * Υποχρεωτική επαφή έκτακτης ανάγκης. Αν δεν υπάρχει συγγενής,
     * καταχωρείται Κοινωνικός Λειτουργός, Υπεύθυνος Δομής ή Νόμιμος Επίτροπος.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "emergency_first_name", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "emergency_last_name", nullable = false)),
            @AttributeOverride(name = "relationship", column = @Column(name = "emergency_relationship", nullable = false)),
            @AttributeOverride(name = "landlinePhone", column = @Column(name = "emergency_landline_phone")),
            @AttributeOverride(name = "mobileNumber", column = @Column(name = "emergency_mobile_number")),
            @AttributeOverride(name = "email", column = @Column(name = "emergency_email"))
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
