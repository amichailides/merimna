package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.assignment.EmployeeAssignmentStatus;
import io.github.amichailides.merimna.assignment.exception.AssignmentAlreadyCancelledException;
import io.github.amichailides.merimna.assignment.exception.AssignmentNotActiveException;
import io.github.amichailides.merimna.assignment.exception.InvalidAssignmentDateRangeException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "employee_assignments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public void complete(LocalDate endDate) {
        requireActive();
        validateDateRange(endDate);

        this.endDate = endDate;
        this.status = EmployeeAssignmentStatus.COMPLETED;
    }

    public void terminate(LocalDate endDate) {
        requireActive();
        validateDateRange(endDate);

        this.endDate = endDate;
        this.status = EmployeeAssignmentStatus.TERMINATED;
    }

    private void requireActive() {
        if (this.status != EmployeeAssignmentStatus.ACTIVE) {
            throw new AssignmentNotActiveException();
        }
    }

    private void validateDateRange(LocalDate endDate) {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidAssignmentDateRangeException();
        }
    }

    public boolean overlapsWith(LocalDate otherStartDate, LocalDate otherEndDate) {
        LocalDate thisStart = this.startDate;
        LocalDate thisEnd = this.endDate != null ? this.endDate : LocalDate.MAX;

        LocalDate otherEnd = otherEndDate != null ? otherEndDate : LocalDate.MAX;

        return !thisStart.isAfter(otherEnd)
                && !otherStartDate.isAfter(thisEnd);
    }

    public boolean isActive() {
        return this.status == EmployeeAssignmentStatus.ACTIVE;
    }

    public void cancel(LocalDate cancelDate) {
        if (this.status == EmployeeAssignmentStatus.CANCELLED) {
            throw new AssignmentAlreadyCancelledException(id);
        }

        Objects.requireNonNull(cancelDate);

        if (cancelDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Cancel date before start date");
        }

        this.endDate = cancelDate;
        this.status = EmployeeAssignmentStatus.CANCELLED;
    }
}
