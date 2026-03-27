package io.github.amichailides.merimna.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // General
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "error.internal.server"),
    INVALID_INPUT("INVALID_INPUT", HttpStatus.BAD_REQUEST, "error.invalid.input"),
    DATABASE_ERROR("DATABASE_ERROR", HttpStatus.CONFLICT, "error.database.conflict"),
    VALIDATION_FAILED("VALIDATION_FAILED", HttpStatus.BAD_REQUEST, "validation.failed"),
    AT_LEAST_ONE_PHONE_REQUIRED("AT_LEAST_ONE_PHONE_REQUIRED", HttpStatus.valueOf(422), "validation.legalRepresentative.phone.atLeastOneRequired"),
    DOMAIN_RULE_VIOLATION("DOMAIN_RULE_VIOLATION", HttpStatus.BAD_REQUEST, "error.domain.rule.violation"),


    // Beneficiary
    BENEFICIARY_NOT_FOUND_BY_ID("BENEFICIARY_NOT_FOUND", HttpStatus.NOT_FOUND, "error.beneficiary.notFoundById"),
    BENEFICIARY_NOT_FOUND_BY_AMKA("BENEFICIARY_NOT_FOUND_BY_AMKA", HttpStatus.NOT_FOUND, "error.beneficiary.notFoundByAmka"),
    AMKA_ALREADY_EXISTS("AMKA_ALREADY_EXISTS", HttpStatus.CONFLICT, "error.beneficiary.amkaAlreadyExists"),
    AMKA_DATE_MISMATCH("AMKA_DATE_MISMATCH", HttpStatus.BAD_REQUEST, "error.beneficiary.amkaDateMismatch"),
    BENEFICIARY_ALREADY_INACTIVE("BENEFICIARY_ALREADY_INACTIVE", HttpStatus.CONFLICT, "error.beneficiary.alreadyInactive"),
    LEGAL_REPRESENTATIVE_ALREADY_ASSIGNED("LEGAL_REPRESENTATIVE_ALREADY_ASSIGNED", HttpStatus.CONFLICT, "error.legalRepresentative.beneficiary.alreadyAssigned"),

    // LegalRepresentative
    LEGAL_REPRESENTATIVE_NOT_FOUND_BY_ID("LEGAL_REPRESENTATIVE_NOT_FOUND_BY_ID", HttpStatus.NOT_FOUND, "error.legalRepresentative.notFoundById"),
    LEGAL_REPRESENTATIVE_NOT_ASSIGNED("LEGAL_REPRESENTATIVE_NOT_ASSIGNED", HttpStatus.CONFLICT, "error.legalRepresentative.beneficiary.notAssigned"),

    // Allergy
    ALLERGY_NOT_FOUND("ALLERGY_NOT_FOUND", HttpStatus.NOT_FOUND, "error.allergy.notFound"),
    ALLERGY_ALREADY_ASSIGNED("ALLERGY_ALREADY_ASSIGNED", HttpStatus.CONFLICT, "error.allergy.alreadyAssigned"),
    ALLERGY_NOT_OWNED_BY_BENEFICIARY("ALLERGY_NOT_OWNED_BY_BENEFICIARY", HttpStatus.CONFLICT, "error.allergy.notOwnedByBeneficiary"),

    // Medication
    MEDICATION_NOT_FOUND("MEDICATION_NOT_FOUND", HttpStatus.NOT_FOUND, "error.medication.notFound"),
    MEDICATION_ALREADY_ASSIGNED("MEDICATION_ALREADY_ASSIGNED", HttpStatus.CONFLICT, "error.medication.alreadyAssigned"),
    MEDICATION_NOT_OWNED_BY_BENEFICIARY("MEDICATION_NOT_OWNED_BY_BENEFICIARY", HttpStatus.CONFLICT, "error.medication.notOwnedByBeneficiary"),

    // employee
    EMPLOYEE_ALREADY_INACTIVE("EMPLOYEE_ALREADY_INACTIVE", HttpStatus.CONFLICT, "error.employee.alreadyInactive"),
    SAME_EMPLOYEE_POSITION("SAME_EMPLOYEE_POSITION", HttpStatus.BAD_REQUEST, "error.employee.position.same");

    private final String code;           // για logging και debugging
    private final HttpStatus status;     // για Web layer
    private final String messageKey;     // για i18n / frontend
}