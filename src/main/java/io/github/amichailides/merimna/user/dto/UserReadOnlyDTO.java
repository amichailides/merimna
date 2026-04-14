package io.github.amichailides.merimna.user.dto;

import io.github.amichailides.merimna.domain.Role;

public record UserReadOnlyDTO(
        Long id,
        String username,
        String email,
        Role role,
        boolean active
) {}
