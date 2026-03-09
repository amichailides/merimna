package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
import io.github.amichailides.merimna.validation.annotations.ValidDateOfBirth;
import io.github.amichailides.merimna.validation.annotations.ValidFirstName;
import io.github.amichailides.merimna.validation.annotations.ValidLastName;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
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
        @NotBlank(message = "{firstName.required}", groups = FirstOrder.class)
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @NotBlank(message = "{lastName.required}", groups = FirstOrder.class)
        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @NotBlank(message = "{amka.required}", groups = FirstOrder.class)
        @ValidAmka(groups = SecondOrder.class)
        String amka,

        @NotNull(message = "{dob.required}", groups = FirstOrder.class)
        @Past(message = "{dob.past}", groups = FirstOrder.class)
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
