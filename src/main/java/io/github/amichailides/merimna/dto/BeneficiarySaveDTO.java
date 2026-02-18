package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.validation.*;
import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record BeneficiarySaveDTO(
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @ValidAmka(groups = SecondOrder.class)
        String amka,

        @ValidDateOfBirth(groups = SecondOrder.class)
        LocalDate dateOfBirth,

        Boolean isActive,

        @NotNull(message = "{houseUnit.required}")
        HouseUnit houseUnit,

        @Valid
        @NotNull(message = "{address.required}")
        AddressDTO permanentAddress,

        @Valid
        @NotNull(message = "{emergencyContact.required}")
        EmergencyContactDTO emergencyContact
) {}
