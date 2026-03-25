package io.github.amichailides.merimna.beneficiary.dto;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.domain.HouseUnit;
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
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;

/**
 * Η επικύρωση ακολουθεί το {@link ValidationGroupSequence} για σταδιακό έλεγχο σφαλμάτων.
 */
@Builder
public record BeneficiarySaveDTO(
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

        @Schema(description = "Whether the beneficiary is active. Defaults to true if omitted.", example = "true")
        Boolean isActive,

        @Schema(description = "The house unit the beneficiary is assigned to", example = "UNIT_A")
        @NotNull(message = "{houseUnit.required}")
        HouseUnit houseUnit,

        @Schema(description = "Permanent residential address of the beneficiary")
        @Valid
        @NotNull(message = "{address.required}")
        AddressDTO permanentAddress,

        @Schema(description = "Emergency contact person for the beneficiary")
        @Valid
        @NotNull(message = "{emergencyContact.required}")
        EmergencyContactDTO emergencyContact
) {}
