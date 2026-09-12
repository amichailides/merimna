package io.github.amichailides.merimna.houseunit.dto;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record HouseUnitCreateDTO(
        @NotBlank(message = "{houseUnit.code.required}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.HOUSE_UNIT_CODE,
                message = "{houseUnit.code.invalid}", groups = SecondOrder.class)
        String code,

        @NotBlank(message = "{houseUnit.displayName.required}", groups = FirstOrder.class)
        @ValidGreekLatinText(
                extended = true,
                max = 50,
                message = "{houseUnit.displayName.invalid}", groups = SecondOrder.class)
        String displayName,

        @NotBlank(message = "{address.required}", groups = FirstOrder.class)
        @ValidGreekLatinText(
                extended = true,
                max = 255,
                message = "{houseUnit.address.invalid}", groups = SecondOrder.class)
        String address,

        @NotNull(message = "{houseUnit.maxCapacity.required}", groups = FirstOrder.class)
        @Positive(message = "{houseUnit.maxCapacity.positive}", groups = SecondOrder.class)
        Integer maxCapacity
) {}
