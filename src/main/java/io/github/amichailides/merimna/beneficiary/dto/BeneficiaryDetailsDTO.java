package io.github.amichailides.merimna.beneficiary.dto;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.allergy.dto.AllergyReadOnlyDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.medication.dto.MedicationReadOnlyDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record BeneficiaryDetailsDTO(
        @Schema(
                description = "Public identifier of the beneficiary",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID publicId,

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

        @Schema(
                description = "Official discharge date. Null while the beneficiary is active.",
                example = "2026-05-03",
                nullable = true
        )
        LocalDate dischargeDate,

        @Schema(
                description = "Reason for discharging the beneficiary. Null while the beneficiary is active.",
                example = "Completion of supported living services.",
                nullable = true
        )
        String dischargeReason,

        @Schema(
                description = "Public identifier of the employee who recorded the discharge. Null while the beneficiary is active.",
                example = "7b9f0e0a-6e1a-4e8c-9f61-5f5f2f7f5d3a",
                nullable = true
        )
        UUID dischargedByEmployeePublicId,

        @Schema(
                description = "Full name of the employee who recorded the discharge. Null while the beneficiary is active.",
                example = "John Doe",
                nullable = true
        )
        String dischargedByEmployeeFullName,

        @Schema(description = "Assigned house unit code", example = "UNIT_A")
        String houseUnitCode,

        @Schema(description = "Assigned house unit display name", example = "ΣΥΔ 2")
        String houseUnitDisplayName,

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