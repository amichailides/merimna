package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.LegalRepresentativeType;
import io.github.amichailides.merimna.validation.annotations.*;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.github.amichailides.merimna.validation.validators.HasPhoneFields;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@AtLeastOnePhonePresent(message = "{contact.missing}", groups = FirstOrder.class)
@Builder
public record LegalRepresentativeDTO(
        @NotNull(message = "{legalRep.required}", groups = FirstOrder.class )
        LegalRepresentativeType type,

        @NotBlank(message = "{firstName.required}", groups = FirstOrder.class)
        @ValidFirstName(message = "{firstName.invalid}", groups = SecondOrder.class)
        String firstName,

        @NotBlank(message = "{lastName.required}", groups = FirstOrder.class)
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
) implements HasPhoneFields {}
