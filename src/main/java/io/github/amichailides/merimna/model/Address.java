package io.github.amichailides.merimna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    /*
     * Logic: Μια διεύθυνση θεωρείται έγκυρη μόνο αν είναι πλήρης.
     * Επιβάλλουμε NonNull στα βασικά πεδία για να αποτρέψουμε "μισές" διευθύνσεις
     * (π.χ. οδός χωρίς πόλη) οπουδήποτε χρησιμοποιείται η κλάση Address.
     */
    @NonNull
    @Column(nullable = false)
    private String street;

    private String streetNumber; // Optional (μπορεί κάποιο σπίτι να είναι χωρίς νούμερο)

    @NonNull
    @Column(nullable = false)
    private String city;

    @NonNull
    @Column(nullable = false)
    private String zipCode;
}
