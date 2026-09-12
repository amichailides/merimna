package io.github.amichailides.merimna.user.dto;

import io.github.amichailides.merimna.validation.annotations.ValidEmail;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserUpdateDTO(
        @Schema(description = "User email address", example = "g.papadopoulos@merimna.gr")
        @ValidEmail(groups = SecondOrder.class)
        String email
) {}
