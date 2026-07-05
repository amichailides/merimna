package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.placement.exception.EmployeePlacementAlreadyClosedException;
import io.github.amichailides.merimna.placement.exception.EmployeePlacementInvalidEndDateException;
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
        p.startDate = start;
        p.endDate = end;
        p.reason = Objects.requireNonNull(reason, "reason is required");

        return p;
    }

    public void close(LocalDate endDate) {
        Objects.requireNonNull(endDate, "endDate is required");

        LocalDate today = LocalDate.now();

        if (this.endDate != null && !this.endDate.isAfter(today)) {
            throw new EmployeePlacementAlreadyClosedException(
                    this.publicId,
                    this.endDate
            );
        }

        if (endDate.isBefore(this.startDate)) {
            throw new EmployeePlacementInvalidEndDateException(
                    this.publicId,
                    this.startDate,
                    endDate
            );
        }

        this.endDate = endDate;
    }

    public boolean isActive(LocalDate now) {
        Objects.requireNonNull(now, "now is required");

        return !startDate.isAfter(now) &&
                (endDate == null || !endDate.isBefore(now));
    }

    private static void validateDates(LocalDate start, LocalDate end) {
        Objects.requireNonNull(start, "startDate is required");

        if (end != null && end.isBefore(start)) {
            throw new EmployeePlacementInvalidEndDateException(start, end);
        }
    }


}
