package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.validation.*;
import io.github.amichailides.merimna.validation.annotations.*;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@AtLeastOnePhonePresent
@Builder
public record EmergencyContactDTO(
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @NotBlank(message = "{emergency.relationship.required}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.GREEK_LATIN_TEXT,
                message = "{emergency.relationship.invalid}", groups = SecondOrder.class)
        String relationship,


        @ValidLandline(message = "{emergency.landline.invalid}", groups = SecondOrder.class)
        String landlinePhone,

        @ValidMobile(message = "{emergency.mobile.invalid}", groups = SecondOrder.class)
        String mobileNumber,

        @Email(message = "{emergency.email.invalid}", groups = SecondOrder.class)
        String email,

        @NotNull(message = "{address.required}", groups = FirstOrder.class)
        @Valid // ΑΠΑΡΑΙΤΗΤΟ: Για να ελεγχθούν τα @Pattern μέσα στο AddressDTO
        AddressDTO address
) {}