package io.github.amichailides.merimna.audit;

import java.util.Map;
import java.util.UUID;

public interface AuditableEvent {

    AuditAction action();

    UUID entityPublicId();

    default Map<String, Object> metadata() {
        return Map.of();
    }
}