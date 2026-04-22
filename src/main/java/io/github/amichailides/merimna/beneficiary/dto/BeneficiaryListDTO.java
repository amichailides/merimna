package io.github.amichailides.merimna.beneficiary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record BeneficiaryListDTO(
        @Schema(
                description = "Public identifier of the beneficiary",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        String publicId,

        @Schema(description = "First name", example = "Γεώργιος")
        String firstName,

        @Schema(description = "Last name", example = "Παπαδόπουλος")
        String lastName,

        @Schema(description = "Assigned house unit", example = "UNIT_A")
        String houseUnit,

        @Schema(description = "Whether the beneficiary is currently active", example = "true")
        boolean isActive
) {}
