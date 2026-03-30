package io.github.amichailides.merimna.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

    @Column(nullable = false, unique = true, length = 20)
    private String code; // "UNIT_A", "UNIT_B", "UNIT_C"

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String address;
}
