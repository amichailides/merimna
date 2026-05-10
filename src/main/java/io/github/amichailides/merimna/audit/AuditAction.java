package io.github.amichailides.merimna.audit;

public enum AuditAction {

    BENEFICIARY_CREATED(AuditEntityType.BENEFICIARY),
    BENEFICIARY_UPDATED(AuditEntityType.BENEFICIARY),
    BENEFICIARY_DISCHARGED(AuditEntityType.BENEFICIARY),
    BENEFICIARY_HOUSE_UNIT_CHANGED(AuditEntityType.BENEFICIARY),


    EMPLOYEE_CREATED(AuditEntityType.EMPLOYEE),
    EMPLOYEE_UPDATED(AuditEntityType.EMPLOYEE),
    EMPLOYEE_TERMINATED(AuditEntityType.EMPLOYEE),
    EMPLOYEE_REACTIVATED(AuditEntityType.EMPLOYEE),

    ASSIGNMENT_CREATED(AuditEntityType.EMPLOYEE_ASSIGNMENT),
    ASSIGNMENT_TERMINATED(AuditEntityType.EMPLOYEE_ASSIGNMENT),
    ASSIGNMENT_CANCELLED(AuditEntityType.EMPLOYEE_ASSIGNMENT),

    PLACEMENT_CREATED(AuditEntityType.EMPLOYEE_PLACEMENT),
    PLACEMENT_TERMINATED(AuditEntityType.EMPLOYEE_PLACEMENT),

    USER_UPDATED(AuditEntityType.USER),

    AUTH_LOGIN_SUCCESS(AuditEntityType.AUTH),
    AUTH_LOGIN_FAILED(AuditEntityType.AUTH),
    AUTH_LOGOUT(AuditEntityType.AUTH);

    private final AuditEntityType entityType;

    AuditAction(AuditEntityType entityType) {
        this.entityType = entityType;
    }

    public AuditEntityType getEntityType() {
        return entityType;
    }
}
