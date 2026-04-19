package io.github.amichailides.merimna.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record UserUpdateDTO(
        @Schema(description = "User email address", example = "g.papadopoulos@merimna.gr")
        @Email(message = "{email.invalid}")
        String email
) {}
