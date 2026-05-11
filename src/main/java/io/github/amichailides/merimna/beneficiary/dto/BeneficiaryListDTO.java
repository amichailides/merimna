package io.github.amichailides.merimna.beneficiary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record BeneficiaryListDTO(
        @Schema(
                description = "Public identifier of the beneficiary",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID publicId,

        @Schema(description = "First name", example = "Γεώργιος")
        String firstName,

        @Schema(description = "Last name", example = "Παπαδόπουλος")
        String lastName,

        @Schema(
                description = "Public identifier of the assigned house unit",
                example = "7d3a4b9e-4e78-4e9d-8a2d-21dcbf4a7f10"
        )
        UUID houseUnitPublicId,

        @Schema(
                description = "Code of the assigned house unit",
                example = "UNIT_A"
        )
        String houseUnitCode,

        @Schema(
                description = "Display name of the assigned house unit",
                example = "House Unit A"
        )
        String houseUnitDisplayName,

        @Schema(description = "Whether the beneficiary is currently active", example = "true")
        boolean isActive
) {}
