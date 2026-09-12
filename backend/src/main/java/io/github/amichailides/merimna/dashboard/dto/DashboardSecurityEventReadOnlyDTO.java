package io.github.amichailides.merimna.dashboard.dto;

import io.github.amichailides.merimna.audit.AuditAction;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record DashboardSecurityEventReadOnlyDTO(
        UUID publicId,
        AuditAction action,
        UUID entityPublicId,
        Instant occurredAt,
        UUID userPublicId,
        UUID employeePublicId,
        Map<String, Object> metadata
) {}
