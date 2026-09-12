package io.github.amichailides.merimna.medication.dto;

import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MedicationCreateDTO(
        @NotBlank(message = "{medication.name.required}", groups = FirstOrder.class)
        @ValidGreekLatinText(message = "{medication.name.invalid}", groups = SecondOrder.class)
        String name,

        @NotBlank(message = "{medication.dosage.required}", groups = FirstOrder.class)
        @ValidGreekLatinText(message = "{medication.dosage.size}", extended = true, groups = SecondOrder.class)
        String dosage,

        @NotBlank(message = "{medication.frequency.required}", groups = FirstOrder.class)
        @ValidGreekLatinText(message = "{medication.frequency.size}", extended = true, groups = SecondOrder.class)
        String frequency,

        // TODO(#44): Revisit administrationTimes as structured time-of-day data.
        @NotBlank(message = "{medication.times.required}", groups = FirstOrder.class)
        @ValidGreekLatinText(message = "{medication.administrationTimes.size}", extended = true, groups = SecondOrder.class)
        String administrationTimes,

        @ValidGreekLatinText(
                min = 0,
                max = 500,
                message = "{medication.instructions.size}",
                extended = true,
                groups = SecondOrder.class
        )
        String instructions,

        @NotNull(message = "{medication.startedAt.required}", groups = FirstOrder.class)
        LocalDate startedAt
) {}