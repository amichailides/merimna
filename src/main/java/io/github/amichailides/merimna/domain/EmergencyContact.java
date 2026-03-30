package io.github.amichailides.merimna.domain;

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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipType relationshipType; // γονέας, δικαστικός συμπαραστάτης κλπ

    private String landlinePhone;
    private String mobileNumber;
    private String email;

    @NonNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "emergency_street",nullable = false)),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "emergency_street_number")),
            @AttributeOverride(name = "city", column = @Column(name = "emergency_city",nullable = false)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "emergency_zip_code", nullable = false))
    })
    private Address address;

}
