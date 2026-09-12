package io.github.amichailides.merimna.user.dto;

import io.github.amichailides.merimna.domain.Role;
import io.github.amichailides.merimna.validation.annotations.ValidEmail;
import io.github.amichailides.merimna.validation.annotations.ValidPassword;
import io.github.amichailides.merimna.validation.annotations.ValidUsername;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserCreateDTO(

        @NotNull(message = "{user.employeeId.required}", groups = FirstOrder.class)
        UUID employeePublicId,

        @NotBlank(message = "{user.username.required}", groups = FirstOrder.class)
        @ValidUsername(groups = SecondOrder.class)
        String username,

        @NotBlank(message = "{user.email.required}", groups = FirstOrder.class)
        @ValidEmail(groups = SecondOrder.class)
        String email,

        @NotBlank(message = "{user.password.required}", groups = FirstOrder.class)
        @ValidPassword(groups = SecondOrder.class)
        String password,

        @NotNull(message = "{user.role.required}", groups = FirstOrder.class)
        Role role
) {}
