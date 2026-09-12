package io.github.amichailides.merimna.audit;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100, updatable = false)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 100, updatable = false)
    private AuditEntityType entityType;

    @Nullable
    @Column(name = "entity_public_id", updatable = false)
    private UUID entityPublicId;

    @Column(name = "user_public_id", updatable = false)
    private UUID userPublicId;

    @Column(name = "employee_public_id", updatable = false)
    private UUID employeePublicId;

    @Column(name = "subject_employee_public_id", updatable = false)
    private UUID subjectEmployeePublicId;

    @Column(name = "ip_address", length = 100, updatable = false)
    private String ipAddress;

    @Column(name = "user_agent", length = 500, updatable = false)
    private String userAgent;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50, updatable = false)
    private AuditOutcome outcome;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", updatable = false)
    private Map<String, Object> metadata = Map.of();

    @PrePersist
    protected void onPersist() {
        occurredAt = Instant.now();
    }
}