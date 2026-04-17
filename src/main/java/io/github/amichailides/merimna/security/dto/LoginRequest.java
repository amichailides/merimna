package io.github.amichailides.merimna.security.dto;

public record LoginRequest(
        String email,
        String password
) {}
