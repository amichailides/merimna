package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.medication.exception.MedicationAlreadyAssignedException;
import io.github.amichailides.merimna.medication.exception.MedicationDateRangeInvalidException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "medications")
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Medication {

    private static final String WHITESPACE_SEQUENCE = "\\s+";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    @Builder.Default
    private UUID publicId = UUID.randomUUID();

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String dosage;

    @NonNull
    @Column(nullable = false)
    private String frequency;

    /*
     * Administration times are stored as a comma-separated String
     * to keep the current ElementCollection database mapping simple.
     *
     * TODO: Consider replacing this with List<LocalTime> and an AttributeConverter
     * if medication reminders or mobile push notifications require time-aware logic.
     */
    @NonNull
    @Column(nullable = false)
    private String administrationTimes;

    @Column
    private String instructions;

    @NonNull
    @Setter(AccessLevel.NONE)
    @Column(name = "started_at", nullable = false)
    private LocalDate startedAt;

    @Setter(AccessLevel.NONE)
    @Column(name = "ended_at")
    private LocalDate endedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beneficiary_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Beneficiary beneficiary;

    public void assignToBeneficiary(@NonNull Beneficiary beneficiary) {
        if (this.beneficiary != null && !this.beneficiary.equals(beneficiary)) {
            throw new MedicationAlreadyAssignedException(
                    this.beneficiary.getPublicId(),
                    beneficiary.getPublicId()
            );
        }

        this.beneficiary = beneficiary;
    }

    public boolean isActive() {
        return endedAt == null;
    }

    public void changeStartedAt(LocalDate startedAt) {
        Objects.requireNonNull(startedAt, "startedAt must not be null");

        if (endedAt != null && startedAt.isAfter(endedAt)) {
            throw new MedicationDateRangeInvalidException(
                    publicId,
                    startedAt,
                    endedAt
            );
        }

        this.startedAt = startedAt;
    }

    public boolean discontinue(LocalDate endedAt) {
        Objects.requireNonNull(endedAt, "endedAt must not be null");

        if (this.endedAt != null) {
            return false;
        }

        if (endedAt.isBefore(startedAt)) {
            throw new MedicationDateRangeInvalidException(
                    publicId,
                    startedAt,
                    endedAt
            );
        }

        this.endedAt = endedAt;

        return true;
    }

    @PrePersist
    @PreUpdate
    private void normalizeFields() {
        this.name = normalize(this.name);
        this.dosage = normalize(this.dosage);
        this.frequency = normalize(this.frequency);
        this.administrationTimes = normalize(this.administrationTimes);
        this.instructions = normalize(this.instructions);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }

        return value.trim().replaceAll(WHITESPACE_SEQUENCE, " ");
    }
}
