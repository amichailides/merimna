package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;


@Builder
public record BeneficiaryReadOnlyDTO(
        @Schema(description = "Unique identifier of the beneficiary", example = "42")
        Long id,

        @Schema(description = "First name", example = "Γεώργιος")
        String firstName,

        @Schema(description = "Last name", example = "Παπαδόπουλος")
        String lastName,

        @Schema(description = "11-digit Greek social security number (AMKA)", example = "12345678901")
        String amka,

        @Schema(description = "Date of birth", example = "1965-04-23")
        LocalDate dateOfBirth,

        @Schema(description = "Whether the beneficiary is currently active", example = "true")
        Boolean isActive,

        @Schema(description = "Assigned house unit", example = "UNIT_A")
        HouseUnit houseUnit,

        @Schema(description = "Permanent residential address")
        AddressDTO permanentAddress,

        @Schema(description = "Emergency contact person")
        EmergencyContactDTO emergencyContact,

        @Schema(description = "Current medications of the beneficiary")
        List<MedicationReadOnlyDTO> medications,

        @Schema(description = "Known allergies of the beneficiary")
        List<AllergyReadOnlyDTO> allergies,

        @Schema(description = "Legal representatives of the beneficiary")
        List<LegalRepresentativeReadOnlyDTO> legalRepresentatives
) {}
