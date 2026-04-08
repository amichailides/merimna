package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.assignment.AssignmentType;
import io.github.amichailides.merimna.employee.exception.EmployeeAlreadyActiveException;
import io.github.amichailides.merimna.employee.exception.EmployeeAlreadyTerminatedException;
import io.github.amichailides.merimna.employee.exception.SameEmployeePositionException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @EqualsAndHashCode.Include
    // TODO(#9): Use publicId as immutable identifier for equals/hashCode (avoid mutable field issues)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

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

    // TODO(#15): Replace ManyToMany with explicit EmployeeHouseUnitAssignment entity.
    // Current model does not support primary vs temporary assignments, time boundaries, or access scope.
    // See issue for details.
    @Builder.Default
    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<EmployeeHouseUnitAssignment> assignments = new HashSet<>();


    public void terminate() {
        if (!isActive) {
            throw new EmployeeAlreadyTerminatedException(id);
        }
        this.isActive = false;
    }

    public void reactivate() {
        if (isActive) {
            throw new EmployeeAlreadyActiveException();
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

    public EmployeeHouseUnitAssignment assignToHouseUnit(
            HouseUnit houseUnit,
            AssignmentType assignmentType,
            LocalDate startDate,
            LocalDate endDate
    ) {
        EmployeeHouseUnitAssignment assignment =
                EmployeeHouseUnitAssignment.create(this, houseUnit, assignmentType, startDate, endDate);

        this.assignments.add(assignment);
        return assignment;
    }
}
