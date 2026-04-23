package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.employee.exception.EmployeeAlreadyActiveException;
import io.github.amichailides.merimna.employee.exception.EmployeeAlreadyTerminatedException;
import io.github.amichailides.merimna.employee.exception.EmployeeHasActiveAssignmentsException;
import io.github.amichailides.merimna.employee.exception.SameEmployeePositionException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
            throw new EmployeeAlreadyActiveException();
        }

        boolean hasActiveAssignments = assignments.stream()
                .anyMatch(EmployeeAssignment::isActive);
        if (hasActiveAssignments) {
            throw new EmployeeHasActiveAssignmentsException();
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
}
