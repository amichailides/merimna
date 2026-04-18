package io.github.amichailides.merimna.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "employee_positions")
public class EmployeePosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private EmployeePositionCode code; // e.g. CAREGIVER

    @Column(nullable = false)
    private String displayName; // e.g. Caregiver

    @Column(nullable = false)
    private boolean requiresExclusivePlacement;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "employee_position_permissions",
            joinColumns = @JoinColumn(name = "position_id")
    )
    @Column(name = "permission", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions = new HashSet<>();

    // future:
    // private String description;
    // private boolean clinicalRole;
}
