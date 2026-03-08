package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.RelationshipType;
import io.github.amichailides.merimna.validation.annotations.ValidFirstName;
import io.github.amichailides.merimna.validation.annotations.ValidLandline;
import io.github.amichailides.merimna.validation.annotations.ValidLastName;
import io.github.amichailides.merimna.validation.annotations.ValidMobile;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;


public record EmergencyContactUpdateDTO(
        @ValidFirstName(message = "{emergency.firstName.invalid}", groups = SecondOrder.class)
        String firstName,

        @ValidLastName(message = "{emergency.lastName.invalid}", groups = SecondOrder.class)
        String lastName,

        RelationshipType relationshipType,

        @ValidLandline(message = "{emergency.landline.invalid}", groups = SecondOrder.class)
        String landlinePhone,

        @ValidMobile(message = "{emergency.mobile.invalid}", groups = SecondOrder.class)
        String mobileNumber,

        @Email(message = "{emergency.email.invalid}", groups = SecondOrder.class)
        String email,

        @Valid
        AddressUpdateDTO address
) {}
