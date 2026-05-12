package io.github.amichailides.merimna.legalrepresentative.dto;

import io.github.amichailides.merimna.domain.LegalRepresentativeType;
import io.github.amichailides.merimna.validation.annotations.*;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.github.amichailides.merimna.validation.HasPhoneFields;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@AtLeastOnePhonePresent(message = "{contact.missing}", groups = FirstOrder.class)
@Builder
public record LegalRepresentativeCreateDTO(
        @NotBlank(message = "{afm.required}", groups = FirstOrder.class)
        @ValidAfm(groups = SecondOrder.class)
        String afm,

        @NotNull(message = "{legalRepresentative.required}", groups = FirstOrder.class)
        LegalRepresentativeType type,

        @NotBlank(message = "{firstName.required}", groups = FirstOrder.class)
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @NotBlank(message = "{lastName.required}", groups = FirstOrder.class)
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
) implements HasPhoneFields {}
