package io.github.amichailides.merimna.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final AuditService auditService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(AuditableEvent event) {
        try {
            auditService.record(event);
        } catch (Exception ex) {
            log.error("Failed to record audit event: {}", event.action(), ex);
        }
    }
}