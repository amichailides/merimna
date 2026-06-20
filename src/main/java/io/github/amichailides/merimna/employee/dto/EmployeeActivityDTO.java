package io.github.amichailides.merimna.employee.dto;

import io.github.amichailides.merimna.audit.AuditAction;
import io.github.amichailides.merimna.audit.AuditEntityType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Schema(name = "EmployeeActivityDTO", description = "Recent activity about an employee")
public record EmployeeActivityDTO(

        @Schema(
                description = "Audit log public identifier",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID publicId,

        @Schema(
                description = "Audit action that occurred",
                example = "EMPLOYEE_UPDATED"
        )
        AuditAction action,

        @Schema(
                description = "Type of domain entity affected by the action",
                example = "EMPLOYEE"
        )
        AuditEntityType entityType,

        @Schema(
                description = "Public identifier of the domain entity that changed",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID entityPublicId,

        @Schema(
                description = "Timestamp when the activity occurred",
                example = "2026-06-20T12:30:00Z"
        )
        Instant occurredAt,

        @Schema(description = "Event-specific structured metadata")
        Map<String, Object> metadata
) {
}