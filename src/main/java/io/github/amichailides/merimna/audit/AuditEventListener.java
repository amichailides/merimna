package io.github.amichailides.merimna.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final AuditService auditService;

    @EventListener
    public void on(AuditableEvent event) {
        auditService.record(event);
    }
}
