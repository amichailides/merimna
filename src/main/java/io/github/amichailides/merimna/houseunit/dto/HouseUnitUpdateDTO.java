package io.github.amichailides.merimna.houseunit.dto;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record HouseUnitUpdateDTO(

        @Pattern(regexp = ValidationPatterns.HOUSE_UNIT_CODE, message = "{houseUnit.code.invalid}", groups = SecondOrder.class)
        String code,

        @Size(max = 100, message = "{houseUnit.displayName.size}", groups = SecondOrder.class)
        String displayName,

        @Size(max = 255, message = "{houseUnit.address.size}", groups = SecondOrder.class)
        String address
) {}
