package io.github.amichailides.merimna.audit;

import java.util.UUID;

public interface AuditContext {

    UUID getUserPublicId();

    UUID getEmployeePublicId();

    String getIpAddress();

    String getUserAgent();
}