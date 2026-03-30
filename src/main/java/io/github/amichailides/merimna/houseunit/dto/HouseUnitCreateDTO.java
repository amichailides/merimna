package io.github.amichailides.merimna.houseunit.dto;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record HouseUnitCreateDTO(
        @NotBlank(message = "{houseUnit.code.required}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.HOUSE_UNIT_CODE, message = "{houseUnit.code.invalid}", groups = SecondOrder.class)
        String code,

        @NotBlank(message = "{houseUnit.displayName.required}", groups = FirstOrder.class)
        @Size(max = 100, message = "{houseUnit.displayName.size}", groups = SecondOrder.class)
        String displayName,

        @NotBlank(message = "{address.required}",groups = FirstOrder.class)
        String address
) {}
