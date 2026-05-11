package io.github.amichailides.merimna.beneficiary.dto;

import io.github.amichailides.merimna.address.dto.AddressUpdateDTO;
import io.github.amichailides.merimna.domain.RelationshipType;
import io.github.amichailides.merimna.validation.annotations.*;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.Valid;


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

        @ValidEmail(message = "{emergency.email.invalid}", groups = SecondOrder.class)
        String email,

        @Valid
        AddressUpdateDTO address
) {}
