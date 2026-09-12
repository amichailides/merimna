package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.assignment.EmployeeAssignmentStatus;
import io.github.amichailides.merimna.assignment.exception.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "employee_assignments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, updatable = false, unique = true)
    private UUID publicId = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "house_unit_id", nullable = false)
    private HouseUnit houseUnit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EmployeeAssignmentStatus status;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    public static EmployeeAssignment create(
            Employee employee,
            HouseUnit houseUnit,
            LocalDate startDate,
            LocalDate endDate
    ) {
        EmployeeAssignment a = new EmployeeAssignment();
        a.employee = Objects.requireNonNull(employee);
        a.houseUnit = Objects.requireNonNull(houseUnit);
        a.startDate = Objects.requireNonNull(startDate);
        a.endDate = endDate;
        a.status = EmployeeAssignmentStatus.ACTIVE;

        a.validateDateRange(endDate);

        return a;
    }

    public void terminate(LocalDate endDate) {
        Objects.requireNonNull(endDate, "endDate must not be null");

        if (this.status != EmployeeAssignmentStatus.ACTIVE) {
            throw new AssignmentTerminationNotAllowedException(
                    this.publicId,
                    this.status
            );
        }

        if (endDate.isBefore(this.startDate)) {
            throw new AssignmentEndDateBeforeStartDateException(
                    this.publicId,
                    this.startDate,
                    endDate
            );
        }

        this.endDate = endDate;
        this.status = EmployeeAssignmentStatus.TERMINATED;
    }

    private void validateDateRange(LocalDate endDate) {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidAssignmentDateRangeException(
                    this.startDate,
                    endDate
            );
        }
    }

    public boolean isActive() {
        return this.status == EmployeeAssignmentStatus.ACTIVE;
    }

    public void cancel(LocalDate cancelDate) {
        Objects.requireNonNull(cancelDate, "cancelDate must not be null");

        if (this.status != EmployeeAssignmentStatus.ACTIVE) {
            throw new AssignmentCancellationNotAllowedException(
                    this.publicId,
                    this.status);
        }

        if (cancelDate.isBefore(this.startDate)) {
            throw new AssignmentCancelDateBeforeStartDateException(
                    this.publicId,
                    this.startDate,
                    cancelDate
            );
        }

        this.endDate = cancelDate;
        this.status = EmployeeAssignmentStatus.CANCELLED;
    }
}
