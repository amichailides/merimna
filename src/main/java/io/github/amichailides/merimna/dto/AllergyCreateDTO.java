package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.AllergySeverity;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AllergyCreateDTO(
        @NotBlank(message = "{allergy.substance.required}", groups = FirstOrder.class)
        @ValidGreekLatinText(max = 50, message = "{allergy.substance.invalid}", groups = SecondOrder.class)
        String substance,

        @NotNull(message = "{allergy.severity.required}", groups = FirstOrder.class)
        AllergySeverity severity,

        @NotBlank(message = "{allergy.reaction.required}", groups = FirstOrder.class)
        @ValidGreekLatinText(max = 200, message = "{allergy.reaction.invalid}", groups = SecondOrder.class)
        String reaction
) {}
