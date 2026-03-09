package io.github.amichailides.merimna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LegalRepresentative {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LegalRepresentativeType type;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String mobileNumber;
    private String landlinePhone;
    private String email;
    private String notes;
}
