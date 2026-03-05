package io.github.amichailides.merimna.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // General
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "error.internal.server"),
    INVALID_INPUT("INVALID_INPUT", HttpStatus.BAD_REQUEST, "error.invalid_input"),
    DATABASE_ERROR("DATABASE_ERROR", HttpStatus.CONFLICT, "error.database.conflict"),
    VALIDATION_FAILED("VALIDATION_FAILED", HttpStatus.BAD_REQUEST, "validation.failed"),


    // Beneficiary
    BENEFICIARY_NOT_FOUND_BY_ID("BENEFICIARY_NOT_FOUND", HttpStatus.NOT_FOUND, "beneficiary.notFoundById"),
    BENEFICIARY_NOT_FOUND_BY_AMKA("BENEFICIARY_NOT_FOUND_BY_AMKA", HttpStatus.NOT_FOUND, "beneficiary.notFoundByAmka"),
    AMKA_ALREADY_EXISTS("AMKA_ALREADY_EXISTS", HttpStatus.CONFLICT, "beneficiary.amkaAlreadyExists"),
    AMKA_DATE_MISMATCH("AMKA_DATE_MISMATCH", HttpStatus.BAD_REQUEST, "beneficiary.amkaDateMismatch"),
    BENEFICIARY_ALREADY_INACTIVE("BENEFICIARY_ALREADY_INACTIVE", HttpStatus.CONFLICT, "beneficiary.alreadyInactive"),
    BENEFICIARY_VALIDATION_FAILED("BENEFICIARY_VALIDATION_FAILED", HttpStatus.BAD_REQUEST, "beneficiary.validation.failed"),

    // Allergy
    ALLERGY_NOT_FOUND("ALLERGY_NOT_FOUND", HttpStatus.NOT_FOUND, "allergy.notFound"),
    ALLERGY_ALREADY_ASSIGNED("ALLERGY_ALREADY_ASSIGNED", HttpStatus.CONFLICT, "allergy.alreadyAssigned"),
    ALLERGY_NOT_OWNED_BY_BENEFICIARY("ALLERGY_NOT_OWNED_BY_BENEFICIARY", HttpStatus.CONFLICT, "allergy.notOwnedByBeneficiary"),

    // Medication
    MEDICATION_NOT_FOUND("MEDICATION_NOT_FOUND", HttpStatus.NOT_FOUND, "medication.notFound"),
    MEDICATION_ALREADY_ASSIGNED("MEDICATION_ALREADY_ASSIGNED", HttpStatus.CONFLICT, "medication.alreadyAssigned"),
    MEDICATION_NOT_OWNED_BY_BENEFICIARY("MEDICATION_NOT_OWNED_BY_BENEFICIARY", HttpStatus.CONFLICT, "medication.notOwnedByBeneficiary");

    private final String code;           // για logging και debugging
    private final HttpStatus status;     // για Web layer
    private final String messageKey;     // για i18n / frontend
}