package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.placement.exception.EmployeePlacementAlreadyClosed;
import io.github.amichailides.merimna.placement.exception.EmployeePlacementInvalidEndDate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "employee_placements")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeePlacement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private UUID publicId = UUID.randomUUID();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Employee employee;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private HouseUnit houseUnit;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PlacementReason reason;

    public static EmployeePlacement create(
            Employee employee,
            HouseUnit houseUnit,
            LocalDate start,
            LocalDate end,
            PlacementReason reason
    ) {
        validateDates(start, end);

        EmployeePlacement p = new EmployeePlacement();
        p.employee = Objects.requireNonNull(employee, "employee is required");
        p.houseUnit = Objects.requireNonNull(houseUnit, "houseUnit is required");
        p.startDate = Objects.requireNonNull(start, "startDate is required");
        p.endDate = end;
        p.reason = Objects.requireNonNull(reason, "reason is required");

        return p;
    }

    public void close(LocalDate end) {
        Objects.requireNonNull(end, "endDate is required");

        if (this.endDate != null) {
            throw new EmployeePlacementAlreadyClosed();
        }

        if (end.isBefore(this.startDate)) {
            throw new EmployeePlacementInvalidEndDate();
        }

        this.endDate = end;
    }

    public boolean isActive(LocalDate now) {
        Objects.requireNonNull(now, "now is required");

        return !startDate.isAfter(now) &&
                (endDate == null || !endDate.isBefore(now));
    }

    private static void validateDates(LocalDate start, LocalDate end) {
        if (start == null) {
            throw new IllegalArgumentException("Start cannot be null");
        }

        if (end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("End before start");
        }
    }


}
