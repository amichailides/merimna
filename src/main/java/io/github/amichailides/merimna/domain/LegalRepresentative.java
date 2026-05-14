package io.github.amichailides.merimna.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static io.github.amichailides.merimna.validation.TextNormalizer.normalize;

// TODO: v2 (#4) - Add AMKA, ID number, court decision metadata, and basic sensitive-data handling for LegalRepresentative.
@Entity
@Table(name = "legal_representatives")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LegalRepresentative {

    private static final String WHITESPACE_SEQUENCE = "\\s+";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @Column(nullable = false, unique = true)
    private String afm;

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

    @ManyToMany(mappedBy = "legalRepresentatives")
    @Builder.Default
    private Set<Beneficiary> beneficiaries = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void normalizeFields() {
        this.firstName = normalize(this.firstName);
        this.lastName = normalize(this.lastName);
        this.notes = normalize(this.notes);
    }
}
