package io.github.amichailides.merimna.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BENEFICIARY_NOT_FOUND("BENEFICIARY_NOT_FOUND", "beneficiary not found"),
    AMKA_ALREADY_EXISTS("AMKA_ALREADY_EXISTS", "AMKA already exists"),
    VALIDATION_FAILED("VALIDATION_FAILED", "Validation failed"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error");

    private final String code;
    private final String messageKey;
}
