package io.github.amichailides.merimna.security.auth.dto;

import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(

        @Schema(
                description = "Raw password reset token received from the password reset link.",
                example = "A8xQmZ0pY2N4Q3VwU2VjdXJlUmVzZXRUb2tlbg"
        )
        @NotBlank(message = "{auth.resetToken.required}", groups = FirstOrder.class)
        String token,

        @Schema(
                description = "New password for the account.",
                example = "NewStrongPassword123!"
        )
        @NotBlank(message = "{auth.newPassword.required}", groups = FirstOrder.class)
        @Size(min = 8, max = 100, message = "{auth.newPassword.size}", groups = SecondOrder.class)
        String newPassword
) {}
