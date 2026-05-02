package io.github.amichailides.merimna.audit;

public interface AuditService {

    void record(AuditableEvent event);
}
