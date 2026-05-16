package io.github.amichailides.merimna.domain;

public enum RevocationReason {
    LOGOUT,
    PASSWORD_CHANGE,
    USER_DEACTIVATION,
    ADMIN_REVOKE,
    ROTATED,
    REUSE_DETECTED
}
