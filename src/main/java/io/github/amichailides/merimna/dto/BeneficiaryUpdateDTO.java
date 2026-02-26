package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
import io.github.amichailides.merimna.validation.annotations.ValidDateOfBirth;
import io.github.amichailides.merimna.validation.annotations.ValidFirstName;
import io.github.amichailides.merimna.validation.annotations.ValidLastName;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.time.LocalDate;
import java.util.List;

@Builder
public record BeneficiaryUpdateDTO(
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @ValidAmka(groups = SecondOrder.class)
        String amka,

        @ValidDateOfBirth(groups = SecondOrder.class)
        LocalDate dateOfBirth,

        @NotNull(message = "{houseUnit.required}")
        HouseUnit houseUnit,

        @Valid
        @NotNull(message = "{address.required}")
        AddressDTO permanentAddress,

        @Valid
        @NotNull(message = "{emergencyContact.required}")
        EmergencyContactDTO emergencyContact,

        List<MedicationCreateDTO> medicalTreatment

) {}