package io.github.amichailides.merimna.user.dto;

import io.github.amichailides.merimna.domain.Role;

import java.util.UUID;

public record UserReadOnlyDTO(
        UUID publicId,
        String username,
        String email,
        Role role,
        boolean active
) {}
