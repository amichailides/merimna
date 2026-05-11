package io.github.amichailides.merimna.beneficiary.dto;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
import io.github.amichailides.merimna.validation.annotations.ValidDateOfBirth;
import io.github.amichailides.merimna.validation.annotations.ValidFirstName;
import io.github.amichailides.merimna.validation.annotations.ValidLastName;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;

/**
 * Η επικύρωση ακολουθεί το {@link ValidationGroupSequence} για σταδιακό έλεγχο σφαλμάτων.
 */
@Builder
public record BeneficiaryCreateDTO(
        @Schema(description = "First name of the beneficiary", example = "Γεώργιος")
        @NotBlank(message = "{firstName.required}", groups = FirstOrder.class)
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @Schema(description = "Last name of the beneficiary", example = "Παπαδόπουλος")
        @NotBlank(message = "{lastName.required}", groups = FirstOrder.class)
        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @Schema(description = "11-digit Greek social security number (AMKA)", example = "12345678901")
        @NotBlank(message = "{amka.required}", groups = FirstOrder.class)
        @ValidAmka(groups = SecondOrder.class)
        String amka,

        @Schema(description = "Date of birth (must be in the past)", example = "1965-04-23")
        @NotNull(message = "{dob.required}", groups = FirstOrder.class)
        @Past(message = "{dob.past}", groups = FirstOrder.class)
        @ValidDateOfBirth(groups = SecondOrder.class)
        LocalDate dateOfBirth,

        @Schema(description = "Public identifier of the house unit the beneficiary is assigned to", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "{houseUnit.publicId.notNull}", groups = FirstOrder.class)
        UUID houseUnitPublicId,

        @Schema(description = "Permanent residential address of the beneficiary")
        @NotNull(message = "{address.required}", groups = FirstOrder.class)
        @Valid
        AddressDTO permanentAddress,

        @Schema(description = "Emergency contact person for the beneficiary")
        @NotNull(message = "{emergencyContact.required}", groups = FirstOrder.class)
        @Valid
        EmergencyContactDTO emergencyContact
) {}
