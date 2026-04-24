package io.github.amichailides.merimna.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private UUID publicId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Employee employee;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private HouseUnit houseUnit;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PlacementReason reason;

    public static EmployeePlacement create(
            Employee employee,
            HouseUnit houseUnit,
            LocalDateTime start,
            LocalDateTime end,
            PlacementReason reason
    ) {
        validateDates(start, end);

        EmployeePlacement p = new EmployeePlacement();
        p.employee = Objects.requireNonNull(employee, "employee is required");
        p.houseUnit = Objects.requireNonNull(houseUnit, "houseUnit is required");
        p.startDateTime = Objects.requireNonNull(start, "startDateTime is required");
        p.endDateTime = end;
        p.reason = Objects.requireNonNull(reason, "reason is required");

        return p;
    }

    public void close(LocalDateTime end) {
        if (this.endDateTime != null) {
            throw new IllegalStateException("Placement already closed");
        }

        if (end.isBefore(this.startDateTime)) {
            throw new IllegalArgumentException("End before start");
        }

        this.endDateTime = end;
    }

    public boolean isActive(LocalDateTime now) {
        return !startDateTime.isAfter(now) &&
                (endDateTime == null || now.isBefore(endDateTime));
    }

    private static void validateDates(LocalDateTime start, LocalDateTime end) {
        if (start == null) {
            throw new IllegalArgumentException("Start cannot be null");
        }

        if (end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("End before start");
        }
    }
}
