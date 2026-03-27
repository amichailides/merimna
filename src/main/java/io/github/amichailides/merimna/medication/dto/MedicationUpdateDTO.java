package io.github.amichailides.merimna.medication.dto;

import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.SecondOrder;

public record MedicationUpdateDTO(

        @ValidGreekLatinText(message = "{medication.name.invalid}", groups = SecondOrder.class)
        String name,

        @ValidGreekLatinText (message = "{medication.dosage.size}", extended = true, groups = SecondOrder.class)
        String dosage,

        @ValidGreekLatinText(message = "{medication.frequency.size}", extended = true, groups = SecondOrder.class)
        String frequency,

        @ValidGreekLatinText(message = "{medication.administrationTimes.size}", extended = true, groups = SecondOrder.class)
        String administrationTimes,

        @ValidGreekLatinText(min = 0, max = 500, message = "{medication.instructions.size}", extended = true, groups = SecondOrder.class)
        String instructions
) {}
