package io.github.amichailides.merimna.dashboard.dto;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditEntityType;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record DashboardActivityReadOnlyDTO(
        UUID publicId,
        AuditAction action,
        AuditEntityType entityType,
        UUID entityPublicId,
        Instant occurredAt,
        Map<String, Object> metadata,
        String subjectName
) {}
