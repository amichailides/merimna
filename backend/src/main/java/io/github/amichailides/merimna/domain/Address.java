package io.github.amichailides.merimna.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import static io.github.amichailides.merimna.validation.TextNormalizer.normalize;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Address {
    @NonNull
    @Column(nullable = false)
    private String street;

    private String streetNumber;

    @NonNull
    @Column(nullable = false)
    private String city;

    @NonNull
    @Column(nullable = false)
    private String zipCode;

    @PrePersist
    @PreUpdate
    private void normalizeFields() {
        this.street = normalize(this.street);
        this.streetNumber = normalize(this.streetNumber);
        this.city = normalize(this.city);
    }
}
