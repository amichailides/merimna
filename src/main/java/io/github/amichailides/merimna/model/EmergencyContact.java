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
     * 1. phoneNumber
     * 2. mobileNumber
     * είναι συμπληρωμένο. Δεν επιτρέπεται η αποθήκευση επαφής χωρίς κανένα μέσο επικοινωνίας.
     * Προς το παρόν παραμένουν προαιρετικά (χωρίς @NotBlank) μόνο για τη διευκόλυνση του αρχικού development.
     */
    private String phoneNumber;
    private String mobileNumber;
    private String email;


    /* * Σημείωση Logic: Χρησιμοποιούμε την Address ως Embedded.
     * Αν υπάρχει επαφή, η διεύθυνση είναι επίσης υποχρεωτική για να ξέρουμε
     * πού να αναζητήσουμε τον υπεύθυνο.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "emergency_street")),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "emergency_number")),
            @AttributeOverride(name = "city", column = @Column(name = "emergency_city")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "emergency_zip"))
    })
    private Address address;

}
