package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.employee.exception.EmployeeAlreadyInactiveException;
import io.github.amichailides.merimna.employee.exception.SameEmployeePositionException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeePosition position;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;



    public void deactivate() {
        if (!isActive) {
            throw new EmployeeAlreadyInactiveException();
        }
        this.isActive = false;
    }

    public void changePosition(EmployeePosition newPosition) {
        if (this.position == newPosition) {
            throw new SameEmployeePositionException(this.position);
        }

        this.position = newPosition;
    }

}
