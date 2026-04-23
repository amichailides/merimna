package io.github.amichailides.merimna.domain;

import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "house_units")
public class HouseUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "public_id", nullable = false, updatable = false)
    @Builder.Default
    private UUID publicId = UUID.randomUUID();

    @Column(nullable = false, unique = true, length = 20)
    private String code; // "UNIT_A", "UNIT_B", "UNIT_C"

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "houseUnit")
    private Set<EmployeeAssignment> assignments = new HashSet<>();

    @Column(nullable = false)
    private int maxCapacity;

    public boolean isFull(long beneficiaryCount) {
        return beneficiaryCount >= maxCapacity;
    }
}


