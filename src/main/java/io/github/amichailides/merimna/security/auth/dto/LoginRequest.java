package io.github.amichailides.merimna.security.auth.dto;

public record LoginRequest(
        String email,
        String password
) {}
