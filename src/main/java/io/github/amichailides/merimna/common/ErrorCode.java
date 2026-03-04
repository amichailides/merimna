package io.github.amichailides.merimna.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // General
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error"),
    INVALID_INPUT("INVALID_INPUT","error.invalid_input"),
    DATABASE_ERROR("DATABASE_ERROR", "error.database.conflict"),
    VALIDATION_FAILED("VALIDATION_FAILED", "Validation failed"),

    // Beneficiary
    BENEFICIARY_NOT_FOUND("BENEFICIARY_NOT_FOUND", "beneficiary not found"),
    AMKA_ALREADY_EXISTS("AMKA_ALREADY_EXISTS", "beneficiary.amkaAlreadyExists"),
    AMKA_DATE_MISMATCH("AMKA_DATE_MISMATCH", "beneficiary.amkaDateMismatch"),
    BENEFICIARY_ALREADY_INACTIVE("ALREADY_INACTIVE", "beneficiary.alreadyInactive"),

    // Allergy
    ALLERGY_NOT_FOUND("ALLERGY_NOT_FOUND", "allergy.notFound"),
    ALLERGY_NOT_OWNED_BY_BENEFICIARY("ALLERGY_NOT_OWNED_BY_BENEFICIARY", "allergy.notOwnedByBeneficiary"),

    // Medication
    MEDICATION_NOT_FOUND("MEDICATION_NOT_FOUND", "medication.notFound"),
    MEDICATION_NOT_OWNED_BY_BENEFICIARY("MEDICATION_NOT_OWNED_BY_BENEFICIARY", "medication.notOwnedByBeneficiary");

    private final String code;
    private final String messageKey;
}
