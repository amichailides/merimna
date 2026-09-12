package io.github.amichailides.merimna.user.dto;

import io.github.amichailides.merimna.validation.annotations.ValidPassword;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDTO(
        @NotBlank(message = "{password.current.required}", groups = FirstOrder.class)
        String currentPassword,

        @NotBlank(message = "{password.new.required}", groups = FirstOrder.class)
        @ValidPassword(groups = SecondOrder.class)
        String newPassword
) {}
