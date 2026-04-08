package io.github.amichailides.merimna.domain;

import jakarta.persistence.*;
import lombok.*;


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

    // future:
    // private String description;
    // private boolean clinicalRole;


}
