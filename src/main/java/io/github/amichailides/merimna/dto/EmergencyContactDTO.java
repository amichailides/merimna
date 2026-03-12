package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.RelationshipType;

import io.github.amichailides.merimna.validation.annotations.*;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.github.amichailides.merimna.validation.validators.HasPhoneFields;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@AtLeastOnePhonePresent(message = "{contact.missing}", groups = FirstOrder.class)
@Builder
public record EmergencyContactDTO(
        @NotBlank(message = "{emergency.firstName.required}", groups = FirstOrder.class)
        @ValidFirstName(message = "{emergency.firstName.invalid}", groups = SecondOrder.class)
        String firstName,

        @NotBlank(message = "{emergency.lastName.required}", groups = FirstOrder.class)
        @ValidLastName(message = "{emergency.lastName.invalid}", groups = SecondOrder.class)
        String lastName,

        @NotNull(message = "{emergency.relationship.required}", groups = FirstOrder.class)
        RelationshipType relationshipType,


        @ValidLandline(message = "{emergency.landline.invalid}", groups = SecondOrder.class)
        String landlinePhone,

        @ValidMobile(message = "{emergency.mobile.invalid}", groups = SecondOrder.class)
        String mobileNumber,

        @Email(message = "{emergency.email.invalid}", groups = SecondOrder.class)
        String email,

        @NotNull(message = "{address.required}", groups = FirstOrder.class)
        @Valid
        AddressDTO address
) implements HasPhoneFields {}