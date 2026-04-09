package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.assignment.AssignmentType;
import io.github.amichailides.merimna.assignment.EmployeeAssignmentStatus;
import io.github.amichailides.merimna.assignment.exception.AssignmentAlreadyCancelledException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "employee_house_unit_assignments")
public class EmployeeHouseUnitAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "house_unit_id", nullable = false)
    private HouseUnit houseUnit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentType assignmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeAssignmentStatus status;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate; // null = ανοιχτό τέλος (για PRIMARY κυρίως)


    public static EmployeeHouseUnitAssignment create(
            Employee employee,
            HouseUnit houseUnit,
            AssignmentType assignmentType,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (employee == null) throw new IllegalArgumentException("employee is required");
        if (houseUnit == null) throw new IllegalArgumentException("houseUnit is required");
        if (assignmentType == null) throw new IllegalArgumentException("assignmentType is required");
        if (startDate == null) throw new IllegalArgumentException("startDate is required");
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate cannot be before startDate");
        }

        EmployeeHouseUnitAssignment a = new EmployeeHouseUnitAssignment();
        a.employee = employee;
        a.houseUnit = houseUnit;
        a.assignmentType = assignmentType;
        a.status = EmployeeAssignmentStatus.ACTIVE;
        a.startDate = startDate;
        a.endDate = endDate;
        return a;
    }


    public boolean isActiveOn(LocalDate date) {
        boolean starts = !this.startDate.isAfter(date);
        boolean ends = this.endDate == null || !this.endDate.isBefore(date);
        return starts && ends;
    }

    public boolean isOpenEnded() {
        return endDate == null;
    }

    public void endOn(LocalDate newEndDate) {
        if (newEndDate == null) {
            throw new IllegalArgumentException("endDate cannot be null");
        }
        if (newEndDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException("endDate cannot be before startDate");
        }
        this.endDate = newEndDate;
    }

    public boolean overlapsWith(LocalDate otherStartDate, LocalDate otherEndDate) {
        LocalDate thisStart = this.startDate;
        LocalDate thisEnd = this.endDate != null ? this.endDate : LocalDate.MAX;

        LocalDate otherEnd = otherEndDate != null ? otherEndDate : LocalDate.MAX;

        return !thisStart.isAfter(otherEnd)
                && !otherStartDate.isAfter(thisEnd);
    }

    public void cancel() {
        if (this.status == EmployeeAssignmentStatus.CANCELLED) {
            throw new AssignmentAlreadyCancelledException(id);
        }
        this.endDate = LocalDate.now();
        this.status = EmployeeAssignmentStatus.CANCELLED;
    }
}
