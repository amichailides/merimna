package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.employee.exception.EmployeeAlreadyInactiveException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "Employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "emp_street", nullable = false)),
            @AttributeOverride(name = "streetNumber", column = @Column(name = "emp_number", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "emp_city", nullable = false)),
            @AttributeOverride(name = "zipCode", column = @Column(name = "emp_zip", nullable = false))
    })
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

    public void changePosition(EmployeePosition position) {
        this.position = position;
    }

}
