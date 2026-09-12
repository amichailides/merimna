package io.github.amichailides.merimna.employee.systemaccess.dto;

import io.github.amichailides.merimna.employee.systemaccess.EmployeeAccessStatus;

import java.time.Instant;

public record EmployeeAccessDTO(
        EmployeeAccessStatus status,
        String accountEmail,
        Instant invitationExpiresAt
) {}
