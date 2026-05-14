package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryAlreadyInactiveException;
import io.github.amichailides.merimna.beneficiary.exception.BeneficiaryInactiveException;
import io.github.amichailides.merimna.legalrepresentative.exception.LegalRepresentativeAlreadyAssignedException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static io.github.amichailides.merimna.validation.TextNormalizer.normalize;

import static java.util.Collections.unmodifiableSet;

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
        })
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    @Builder.Default
    private UUID publicId = UUID.randomUUID();

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
    private boolean isActive = true;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_unit_id", nullable = false)
    private HouseUnit houseUnit;

    @Column(name = "discharge_date")
    private LocalDate dischargeDate;

    @Column(name = "discharge_reason", length = 500)
    private String dischargeReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discharged_by_employee_id")
    private Employee dischargedBy;

    /*
     * Υποχρεωτική διεύθυνση. Αν ο ωφελούμενος δεν έχει οικογενειακή διεύθυνση,
     * καταχωρείται η διεύθυνση της δομής φιλοξενίας.
     */
    @NonNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "perm_street", nullable = false)),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "perm_street_number", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "perm_city", nullable = false)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "perm_zip_code", nullable = false))
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

    @Setter(AccessLevel.NONE)
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "beneficiary_legal_representatives",
            joinColumns = @JoinColumn(name = "beneficiary_id"),
            inverseJoinColumns = @JoinColumn(name = "legal_representative_id")
    )
    private Set<LegalRepresentative> legalRepresentatives = new HashSet<>();

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
        if (this.medications.remove(medication)) {
            medication.clearBeneficiary();
        }
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

    public void addLegalRepresentative(@NonNull LegalRepresentative legalRepresentative) {
        if (this.legalRepresentatives.contains(legalRepresentative)) {
            throw new LegalRepresentativeAlreadyAssignedException(
                    legalRepresentative.getId(),
                    this.getPublicId()
            );
        }

        this.legalRepresentatives.add(legalRepresentative);
        legalRepresentative.getBeneficiaries().add(this);

    }

    public void removeLegalRepresentative(@NonNull LegalRepresentative legalRepresentative) {
        this.legalRepresentatives.remove(legalRepresentative);
        legalRepresentative.getBeneficiaries().remove(this);
    }

    public void discharge(
            LocalDate dischargeDate,
            String dischargeReason,
            Employee dischargedBy
    ) {
        if (!isActive) {
            throw new BeneficiaryAlreadyInactiveException(publicId);
        }

        this.isActive = false;
        this.dischargeDate = Objects.requireNonNull(dischargeDate, "dischargeDate must not be null");
        this.dischargeReason = Objects.requireNonNull(dischargeReason, "dischargeReason must not be null");
        this.dischargedBy = Objects.requireNonNull(dischargedBy, "dischargedBy must not be null");
    }

    public void changeHouseUnit(HouseUnit targetHouseUnit) {
        ensureActive();
        this.houseUnit = targetHouseUnit;
    }

    public void ensureActive() {
        if (!this.isActive) {
            throw new BeneficiaryInactiveException(publicId);
        }
    }

    public boolean isAssignedTo(HouseUnit houseUnit) {
        return this.houseUnit.getPublicId().equals(houseUnit.getPublicId());
    }

    public boolean isNotAssignedTo(HouseUnit houseUnit) {
        return !this.houseUnit.getPublicId().equals(houseUnit.getPublicId());
    }

    private static final String WHITESPACE_SEQUENCE = "\\s+";

    @PrePersist
    @PreUpdate
    private void normalizeFields() {
        this.firstName = normalize(this.firstName);
        this.lastName = normalize(this.lastName);
        this.dischargeReason = normalize(this.dischargeReason);
    }
}
