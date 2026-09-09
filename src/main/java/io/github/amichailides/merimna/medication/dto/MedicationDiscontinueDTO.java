package io.github.amichailides.merimna.medication.dto;

import io.github.amichailides.merimna.validation.groups.FirstOrder;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MedicationDiscontinueDTO(
        @NotNull(message = "{medication.endedAt.required}", groups = FirstOrder.class)
        LocalDate endedAt
) {}