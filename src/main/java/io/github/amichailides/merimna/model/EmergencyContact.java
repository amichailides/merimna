package io.github.amichailides.merimna.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class EmergencyContact {
    @NonNull
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;

    @NonNull
    @Column(nullable = false)
    private String relationship; // γονεας, δικαστικος συμπαραστατης κλπ

    /**
     * TODO: ΥΠΟΧΡΕΩΤΙΚΗ ΥΛΟΠΟΙΗΣΗ (Business Requirement):
     * Πρέπει να προστεθεί Class-level Validator (π.χ. @AtLeastOneContactPresent)
     * που να διασφαλίζει ότι ΤΟΥΛΑΧΙΣΤΟΝ ΕΝΑ από τα πεδία:
     * 1. landlinePhone
     * 2. mobileNumber
     * είναι συμπληρωμένο. Δεν επιτρέπεται η αποθήκευση επαφής χωρίς κανένα μέσο επικοινωνίας.
     * Προς το παρόν παραμένουν προαιρετικά (χωρίς @NotBlank) μόνο για τη διευκόλυνση του αρχικού development.
     */
    private String landlinePhone;
    private String mobileNumber;
    private String email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "emergency_street",nullable = false)),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "emergency_number",nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "emergency_city",nullable = false)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "emergency_zip", nullable = false))
    })
    private Address address;

}
