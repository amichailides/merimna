package io.github.amichailides.merimna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Medication {
    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String dosage;

    @NonNull
    @Column(nullable = false)
    private String frequency;

    @NonNull
    @Column(nullable = false)
    /*
     * Logic: Οι ώρες αποθηκεύονται ως String (π.χ. "08:00, 20:00") για να είναι
     * συμβατές με το database mapping των ElementCollections.
     * TODO: Στο μέλλον μπορεί να μετατραπεί σε List<LocalTime> μέσω AttributeConverter
     * για την υποστήριξη push notifications στο mobile app.
     */
    private String administrationTimes;

    private String instructions;
}
