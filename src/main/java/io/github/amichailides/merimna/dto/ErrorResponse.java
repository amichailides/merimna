package io.github.amichailides.merimna.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp
        // TODO: Add error code field when ready
        //  e.g., String code (BENEFICIARY_NOT_FOUND, AMKA_ALREADY_EXISTS, etc.)
        //  Benefits: Frontend can handle errors programmatically, i18n-safe
) {}