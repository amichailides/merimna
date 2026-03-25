package io.github.amichailides.merimna.beneficiary.dto;

import io.github.amichailides.merimna.address.dto.AddressUpdateDTO;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
import io.github.amichailides.merimna.validation.annotations.ValidDateOfBirth;
import io.github.amichailides.merimna.validation.annotations.ValidFirstName;
import io.github.amichailides.merimna.validation.annotations.ValidLastName;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record BeneficiaryUpdateDTO(
        @Schema(description = "Updated first name", example = "Γεώργιος")
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @Schema(description = "Updated last name", example = "Παπαδόπουλος")
        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @Schema(description = "Updated AMKA — 11-digit Greek social security number", example = "12345678901")
        @ValidAmka(groups = SecondOrder.class)
        String amka,

        @Schema(description = "Updated date of birth", example = "1965-04-23")
        @ValidDateOfBirth(groups = SecondOrder.class)
        LocalDate dateOfBirth,

        @Schema(description = "Updated house unit assignment", example = "UNIT_B")
        HouseUnit houseUnit,

        @Schema(description = "Updated permanent address. Only provided fields will be changed.")
        @Valid
        AddressUpdateDTO permanentAddress,

        @Schema(description = "Updated emergency contact. Only provided fields will be changed.")
        @Valid
        EmergencyContactUpdateDTO emergencyContact
) {}