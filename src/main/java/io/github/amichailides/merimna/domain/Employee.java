package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.employee.exception.EmployeeAlreadyActiveException;
import io.github.amichailides.merimna.employee.exception.EmployeeAlreadyTerminatedException;
import io.github.amichailides.merimna.employee.exception.EmployeeHasActiveAssignmentsException;
import io.github.amichailides.merimna.employee.exception.SameEmployeePositionException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static io.github.amichailides.merimna.validation.TextNormalizer.normalize;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Builder.Default
    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private UUID publicId = UUID.randomUUID();

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true)
    private String contactEmail;

    @Column(nullable = false)
    private String mobileNumber;

    @Embedded
    @NotNull
    private Address address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "position_id", nullable = false)
    private EmployeePosition position;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = false)
    private String emergencyContactName;

    @Column(nullable = false)
    private String emergencyContactPhoneNumber;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Builder.Default
    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<EmployeeAssignment> assignments = new HashSet<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "employee",
            fetch = FetchType.LAZY
    )
    private List<EmployeePlacement> placements = new ArrayList<>();


    public void terminate(LocalDate terminationDate) {
        if (!isActive) {
            throw new EmployeeAlreadyTerminatedException(id);
        }
        this.isActive = false;

        assignments.stream()
                .filter(EmployeeAssignment::isActive)
                .forEach(a -> a.terminate(terminationDate));
    }

    public void reactivate() {
        if (isActive) {
            throw new EmployeeAlreadyActiveException(this.publicId);
        }

        boolean hasActiveAssignments = assignments.stream()
                .anyMatch(EmployeeAssignment::isActive);
        if (hasActiveAssignments) {
            throw new EmployeeHasActiveAssignmentsException(this.publicId);
        }
        this.isActive = true;
    }

    public void changePosition(EmployeePosition newPosition) {
        if (this.position != null
                && this.position.getCode().equals(newPosition.getCode())) {
            throw new SameEmployeePositionException(newPosition.getCode().getValue());
        }

        this.position = newPosition;
    }

    public EmployeeAssignment assignToHouseUnit(
            HouseUnit houseUnit,
            LocalDate startDate,
            LocalDate endDate
    ) {
        EmployeeAssignment assignment =
                EmployeeAssignment.create(this, houseUnit, startDate, endDate);

        this.assignments.add(assignment);
        return assignment;
    }

    public void addPlacement(EmployeePlacement placement) {
        placements.add(placement);
    }

    /**
     * Returns all house units this employee can access,
     * based on active assignments and active placements.
     * <p>
     * NOTE: Uses in-memory filtering — acceptable given the low volume
     * of assignments/placements per employee. If this becomes a
     * performance concern, consider dedicated repository queries.
     */
    public Set<HouseUnit> getAccessibleHouseUnits(LocalDate today) {
        Set<HouseUnit> accessible = new HashSet<>();

        assignments.stream()
                .filter(EmployeeAssignment::isActive)
                .map(EmployeeAssignment::getHouseUnit)
                .forEach(accessible::add);

        placements.stream()
                .filter(p -> p.isActive(today))
                .map(EmployeePlacement::getHouseUnit)
                .forEach(accessible::add);

        return accessible;
    }

    private static final String WHITESPACE_SEQUENCE = "\\s+";

    @PrePersist
    @PreUpdate
    private void normalizeFields() {
        this.firstName = normalize(this.firstName);
        this.lastName = normalize(this.lastName);
    }
}
