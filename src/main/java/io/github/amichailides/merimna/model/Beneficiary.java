package io.github.amichailides.merimna.model;

import io.github.amichailides.merimna.exception.BeneficiaryAlreadyInactiveException;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

import static java.util.Collections.unmodifiableSet;

/**
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

    @Setter(AccessLevel.NONE)
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
    @NonNull
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
     * μπορεί να καταχωρηθεί Κοινωνικός Λειτουργός ή Υπεύθυνος Δομής.
     */
    @NonNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "emergency_first_name", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "emergency_last_name", nullable = false)),
            @AttributeOverride(name = "relationshipType", column = @Column(name = "emergency_relationship", nullable = false)),
            @AttributeOverride(name = "landlinePhone", column = @Column(name = "emergency_landline_phone")),
            @AttributeOverride(name = "mobileNumber", column = @Column(name = "emergency_mobile_number")),
            @AttributeOverride(name = "email", column = @Column(name = "emergency_email"))
    })
    private EmergencyContact emergencyContact;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "legal_type" )),
            @AttributeOverride(name = "firstName", column = @Column(name = "legal_first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "legal_last_name")),
            @AttributeOverride(name = "mobileNumber", column = @Column(name = "legal_mobile_number")),
            @AttributeOverride(name = "landlinePhone", column = @Column(name = "legal_landline_phone")),
            @AttributeOverride(name = "email", column = @Column(name = "legal_email")),
            @AttributeOverride(name = "notes", column = @Column(name = "legal_notes"))
    })
    private LegalRepresentative legalRepresentative;

    @OneToMany(mappedBy = "beneficiary", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private Set<Medication> medications = new HashSet<>();

    public void addMedication(@NonNull Medication medication) {
        this.medications.add(medication);
        medication.assignToBeneficiary(this);
    }

    public void removeMedication(@NonNull Medication medication) {
        this.medications.remove(medication);
        medication.clearBeneficiary();
    }

    public Set<Medication> getMedications() {
        return unmodifiableSet(medications);
    }

    @OneToMany(mappedBy = "beneficiary", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
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

        return unmodifiableSet(allergies);
    }


    public void discharge() {

        if (!isActive) {
            throw new BeneficiaryAlreadyInactiveException();
        }

        this.isActive = false;
        // TODO: add exitReason, exitDate, approvedBy fields when DischargeDTO is implemented
    }

}
