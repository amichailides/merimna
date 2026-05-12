package io.github.amichailides.merimna.legalrepresentative.dto;

import io.github.amichailides.merimna.domain.LegalRepresentativeType;
import io.github.amichailides.merimna.validation.annotations.*;
import io.github.amichailides.merimna.validation.groups.SecondOrder;

public record LegalRepresentativeUpdateDTO(

        LegalRepresentativeType type,

        @ValidAfm(groups = SecondOrder.class)
        String afm,

        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @ValidMobile(groups = SecondOrder.class)
        String mobileNumber,

        @ValidLandline(groups = SecondOrder.class)
        String landlinePhone,

        @ValidEmail(groups = SecondOrder.class)
        String email,

        @ValidGreekLatinText(
                max = 500,
                message = "{legalRepresentative.notes.invalid}",
                groups = SecondOrder.class)
        String notes
) {}
