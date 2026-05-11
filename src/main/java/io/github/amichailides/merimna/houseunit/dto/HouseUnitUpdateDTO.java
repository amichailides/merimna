package io.github.amichailides.merimna.houseunit.dto;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.OptionalNotBlank;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record HouseUnitUpdateDTO(

        @OptionalNotBlank(message = "{houseUnit.code.notBlank}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.HOUSE_UNIT_CODE,
                message = "{houseUnit.code.invalid}", groups = SecondOrder.class)
        String code,

        @ValidGreekLatinText(
                extended = true,
                max = 50,
                message = "{houseUnit.displayName.invalid}", groups = SecondOrder.class)
        String displayName,

        @ValidGreekLatinText(
                extended = true,
                max = 255, message = "{houseUnit.address.invalid}", groups = SecondOrder.class)
        String address,

        @Positive(message = "{houseUnit.maxCapacity.positive}", groups = SecondOrder.class)
        Integer maxCapacity
) {}
