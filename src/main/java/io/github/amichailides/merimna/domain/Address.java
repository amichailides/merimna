package io.github.amichailides.merimna.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

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
}
