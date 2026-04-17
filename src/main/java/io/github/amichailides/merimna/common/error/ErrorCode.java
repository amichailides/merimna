package io.github.amichailides.merimna.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // =========================================================================
    // System / Infrastructure
    // =========================================================================
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "error.internal.server"),
    INVALID_INPUT("INVALID_INPUT", HttpStatus.BAD_REQUEST, "error.invalid.input"),
    DATABASE_ERROR("DATABASE_ERROR", HttpStatus.CONFLICT, "error.database.conflict"),
    VALIDATION_FAILED("VALIDATION_FAILED", HttpStatus.BAD_REQUEST, "validation.failed"),
    AT_LEAST_ONE_PHONE_REQUIRED("AT_LEAST_ONE_PHONE_REQUIRED", HttpStatus.valueOf(422), "validation.legalRepresentative.phone.atLeastOneRequired"),
    DOMAIN_RULE_VIOLATION("DOMAIN_RULE_VIOLATION", HttpStatus.BAD_REQUEST, "error.domain.rule.violation"),
    RESOURCE_CONFLICT("RESOURCE_CONFLICT", HttpStatus.CONFLICT, "error.resource.conflict"),

    // =========================================================================
    // Security
    // =========================================================================
    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "error.security.unauthorized"),
    ACCOUNT_LOCKED("ACCOUNT_LOCKED", HttpStatus.FORBIDDEN, "error.security.accountLocked"),
    ACCOUNT_DISABLED("ACCOUNT_DISABLED", HttpStatus.FORBIDDEN, "error.security.accountDisabled"),
    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN, "error.security.forbidden"),
    TOKEN_EXPIRED("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED, "error.security.tokenExpired"),
    TOKEN_INVALID("TOKEN_INVALID", HttpStatus.UNAUTHORIZED, "error.security.tokenInvalid"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED, "error.security.invalidCredentials"),
    AUTHENTICATION_FAILED("AUTHENTICATION_FAILED", HttpStatus.UNAUTHORIZED, "error.security.authenticationFailed"),

    // =========================================================================
    // Beneficiary
    // =========================================================================
    BENEFICIARY_NOT_FOUND_BY_ID("BENEFICIARY_NOT_FOUND_BY_ID", HttpStatus.NOT_FOUND, "error.beneficiary.notFoundById"),
    BENEFICIARY_NOT_FOUND_BY_AMKA("BENEFICIARY_NOT_FOUND_BY_AMKA", HttpStatus.NOT_FOUND, "error.beneficiary.notFoundByAmka"),
    AMKA_ALREADY_EXISTS("AMKA_ALREADY_EXISTS", HttpStatus.CONFLICT, "error.beneficiary.amkaAlreadyExists"),
    AMKA_DATE_MISMATCH("AMKA_DATE_MISMATCH", HttpStatus.BAD_REQUEST, "error.beneficiary.amkaDateMismatch"),
    BENEFICIARY_ALREADY_INACTIVE("BENEFICIARY_ALREADY_INACTIVE", HttpStatus.CONFLICT, "error.beneficiary.alreadyInactive"),
    LEGAL_REPRESENTATIVE_ALREADY_ASSIGNED("LEGAL_REPRESENTATIVE_ALREADY_ASSIGNED", HttpStatus.CONFLICT, "error.legalRepresentative.beneficiary.alreadyAssigned"),
    BENEFICIARY_ALREADY_IN_HOUSE_UNIT("BENEFICIARY_ALREADY_IN_HOUSE_UNIT", HttpStatus.CONFLICT, "error.beneficiary.alreadyInHouseUnit"),

    // =========================================================================
    // Legal Representative
    // =========================================================================
    LEGAL_REPRESENTATIVE_NOT_FOUND_BY_ID("LEGAL_REPRESENTATIVE_NOT_FOUND_BY_ID", HttpStatus.NOT_FOUND, "error.legalRepresentative.notFoundById"),
    LEGAL_REPRESENTATIVE_NOT_ASSIGNED("LEGAL_REPRESENTATIVE_NOT_ASSIGNED", HttpStatus.CONFLICT, "error.legalRepresentative.beneficiary.notAssigned"),
    BENEFICIARY_INACTIVE("BENEFICIARY_INACTIVE", HttpStatus.CONFLICT, "error.beneficiary.inactive"),

    // =========================================================================
    // Allergy
    // =========================================================================
    ALLERGY_NOT_FOUND("ALLERGY_NOT_FOUND", HttpStatus.NOT_FOUND, "error.allergy.notFound"),
    ALLERGY_ALREADY_ASSIGNED("ALLERGY_ALREADY_ASSIGNED", HttpStatus.CONFLICT, "error.allergy.alreadyAssigned"),
    ALLERGY_NOT_OWNED_BY_BENEFICIARY("ALLERGY_NOT_OWNED_BY_BENEFICIARY", HttpStatus.CONFLICT, "error.allergy.notOwnedByBeneficiary"),
    ALLERGY_DUPLICATE_SUBSTANCE("ALLERGY_DUPLICATE_SUBSTANCE", HttpStatus.CONFLICT, "error.allergy.duplicateSubstance"),

    // =========================================================================
    // Medication
    // =========================================================================
    MEDICATION_NOT_FOUND("MEDICATION_NOT_FOUND", HttpStatus.NOT_FOUND, "error.medication.notFound"),
    MEDICATION_ALREADY_ASSIGNED("MEDICATION_ALREADY_ASSIGNED", HttpStatus.CONFLICT, "error.medication.alreadyAssigned"),
    MEDICATION_NOT_OWNED_BY_BENEFICIARY("MEDICATION_NOT_OWNED_BY_BENEFICIARY", HttpStatus.CONFLICT, "error.medication.notOwnedByBeneficiary"),

    // =========================================================================
    // Employee
    // =========================================================================
    EMPLOYEE_ALREADY_TERMINATED("EMPLOYEE_ALREADY_TERMINATED", HttpStatus.CONFLICT, "error.employee.alreadyTerminated"),
    SAME_EMPLOYEE_POSITION("SAME_EMPLOYEE_POSITION", HttpStatus.BAD_REQUEST, "error.employee.position.same"),
    EMPLOYEE_NOT_FOUND_BY_ID("EMPLOYEE_NOT_FOUND_BY_ID", HttpStatus.NOT_FOUND, "error.employee.notFoundById"),
    EMPLOYEE_INACTIVE("EMPLOYEE_INACTIVE", HttpStatus.CONFLICT, "error.employee.inactive"),
    EMPLOYEE_EMAIL_ALREADY_EXISTS("EMPLOYEE_EMAIL_ALREADY_EXISTS", HttpStatus.CONFLICT, "error.employee.email.alreadyExists"),
    EMPLOYEE_ALREADY_ACTIVE("EMPLOYEE_ALREADY_ACTIVE", HttpStatus.CONFLICT, "error.employee.alreadyActive"),
    EMPLOYEE_TERMINATION_BEFORE_HIRE_DATE("EMPLOYEE_TERMINATION_BEFORE_HIRE_DATE", HttpStatus.BAD_REQUEST, "error.employee.termination.beforeHireDate"),
    EMPLOYEE_HAS_ACTIVE_ASSIGNMENTS("EMPLOYEE_HAS_ACTIVE_ASSIGNMENTS", HttpStatus.CONFLICT, "error.employee.hasActiveAssignments"),

    // =========================================================================
    // House Unit
    // =========================================================================
    HOUSE_UNIT_NOT_FOUND_BY_CODE("HOUSE_UNIT_NOT_FOUND_BY_CODE", HttpStatus.NOT_FOUND, "error.houseUnit.notFoundByCode"),
    HOUSE_UNIT_ALREADY_EXISTS("HOUSE_UNIT_ALREADY_EXISTS", HttpStatus.CONFLICT, "error.houseUnit.alreadyExists"),
    HOUSE_UNIT_CAPACITY_EXCEEDED("HOUSE_UNIT_CAPACITY_EXCEEDED", HttpStatus.BAD_REQUEST, "error.houseUnit.capacityExceeded"),
    HOUSE_UNIT_FULL("HOUSE_UNIT_FULL", HttpStatus.BAD_REQUEST, "error.houseUnit.full"),

    /// =========================================================================
    // Assignment
    // =========================================================================
    ASSIGNMENT_NOT_FOUND("ASSIGNMENT_NOT_FOUND", HttpStatus.NOT_FOUND, "error.assignment.notFound"),
    ASSIGNMENT_OVERLAP_NOT_ALLOWED("ASSIGNMENT_OVERLAP_NOT_ALLOWED", HttpStatus.BAD_REQUEST, "error.assignment.overlapNotAllowed"),
    ASSIGNMENT_DUPLICATE_ACTIVE_FOR_HOUSE("ASSIGNMENT_DUPLICATE_ACTIVE_FOR_HOUSE", HttpStatus.BAD_REQUEST,"error.assignment.duplicateActiveForHouse"),
    ASSIGNMENT_NOT_ACTIVE("ASSIGNMENT_NOT_ACTIVE", HttpStatus.BAD_REQUEST, "error.assignment.notActive"),
    ASSIGNMENT_INVALID_DATE_RANGE("ASSIGNMENT_INVALID_DATE_RANGE", HttpStatus.BAD_REQUEST, "error.assignment.invalidDateRange"),
    ASSIGNMENT_BEFORE_HIRE_DATE("ASSIGNMENT_BEFORE_HIRE_DATE", HttpStatus.BAD_REQUEST, "error.assignment.beforeHireDate"),
    ASSIGNMENT_CANCELLATION_NOT_ALLOWED("ASSIGNMENT_CANCELLATION_NOT_ALLOWED", HttpStatus.BAD_REQUEST,"error.assignment.cancellationNotAllowed"),
    ASSIGNMENT_CANCEL_DATE_BEFORE_START_DATE("ASSIGNMENT_CANCEL_DATE_BEFORE_START_DATE", HttpStatus.BAD_REQUEST, "error.assignment.cancelDate.beforeStartDate"),
    ASSIGNMENT_TERMINATION_NOT_ALLOWED("ASSIGNMENT_TERMINATION_NOT_ALLOWED", HttpStatus.BAD_REQUEST, "error.assignment.terminationNotAllowed"),
    ASSIGNMENT_END_DATE_BEFORE_START_DATE("ASSIGNMENT_END_DATE_BEFORE_START_DATE", HttpStatus.BAD_REQUEST, "error.assignment.endDate.beforeStartDate"),

    // =========================================================================
    // Employee Position
    // =========================================================================
    EMPLOYEE_POSITION_NOT_FOUND_BY_CODE("EMPLOYEE_POSITION_NOT_FOUND_BY_CODE", HttpStatus.NOT_FOUND, "error.employeePosition.notFoundByCode"),
    EMPLOYEE_POSITION_ALREADY_EXISTS("EMPLOYEE_POSITION_ALREADY_EXISTS", HttpStatus.CONFLICT, "error.employeePosition.alreadyExists"),
    INVALID_EMPLOYEE_POSITION_CODE("INVALID_EMPLOYEE_POSITION_CODE", HttpStatus.BAD_REQUEST, "error.employeePosition.invalidCode"),


    // =========================================================================
    // User
    // =========================================================================
    EMPLOYEE_ALREADY_HAS_ACCOUNT("EMPLOYEE_ALREADY_HAS_ACCOUNT", HttpStatus.CONFLICT, "error.user.employeeAlreadyHasAccount"),
    USERNAME_ALREADY_EXISTS("USERNAME_ALREADY_EXISTS", HttpStatus.CONFLICT, "error.user.usernameAlreadyExists"),
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", HttpStatus.CONFLICT, "error.user.emailAlreadyExists");

    private final String code;           // για logging και debugging
    private final HttpStatus status;     // για Web layer
    private final String messageKey;     // για i18n / frontend
}