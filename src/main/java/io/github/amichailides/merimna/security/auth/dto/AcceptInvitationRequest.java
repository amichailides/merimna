package io.github.amichailides.merimna.security.auth.dto;

import io.github.amichailides.merimna.validation.annotations.ValidPassword;
import io.github.amichailides.merimna.validation.annotations.ValidUsername;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.NotBlank;

public record AcceptInvitationRequest(

        @NotBlank(
                message = "{auth.invitation.token.required}",
                groups = FirstOrder.class
        )
        String token,

        @NotBlank(
                message = "{user.username.required}",
                groups = FirstOrder.class
        )
        @ValidUsername(groups = SecondOrder.class)
        String username,

        @NotBlank(
                message = "{user.password.required}",
                groups = FirstOrder.class
        )
        @ValidPassword(groups = SecondOrder.class)
        String password
) {
}