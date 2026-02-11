package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.validation.ValidAmka;
import io.github.amichailides.merimna.validation.ValidDateOfBirth;
import io.github.amichailides.merimna.validation.ValidFirstName;
import io.github.amichailides.merimna.validation.ValidLastName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record BeneficiarySaveDTO(
        @ValidFirstName
        String firstName,

        @ValidLastName
        String lastName,

        @ValidAmka
        String amka,

        @ValidDateOfBirth
        LocalDate dateOfBirth,

        @NotNull(message = "{houseUnit.required}")
        HouseUnit houseUnit,

        @Valid
        @NotNull(message = "{address.required}")
        AddressDTO permanentAddress,

        @Valid
        @NotNull(message = "{emergencyContact.required}")
        EmergencyContactDTO emergencyContact
) {}
