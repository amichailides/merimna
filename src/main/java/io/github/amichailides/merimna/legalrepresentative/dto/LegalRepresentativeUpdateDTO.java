package io.github.amichailides.merimna.legalrepresentative.dto;

import io.github.amichailides.merimna.domain.LegalRepresentativeType;
import io.github.amichailides.merimna.validation.annotations.*;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.Email;

public record LegalRepresentativeUpdateDTO(

        LegalRepresentativeType type,

        @ValidFirstName(message = "{firstName.invalid}", groups = SecondOrder.class)
        String firstName,

        @ValidLastName(message = "{lastName.invalid}", groups = SecondOrder.class)
        String lastName,

        @ValidMobile(message = "{mobile.invalid}", groups = SecondOrder.class)
        String mobileNumber,

        @ValidLandline(message = "{landline.invalid}", groups = SecondOrder.class)
        String landlinePhone,

        @Email(message = "{email.invalid}", groups = SecondOrder.class)
        String email,

        @ValidGreekLatinText(max =500, message = "{legalRep.notes.invalid}", groups = SecondOrder.class)
        String notes
) {}
