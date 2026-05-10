package io.github.amichailides.merimna.address.dto;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.OptionalNotBlank;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import jakarta.validation.constraints.Pattern;

public record AddressUpdateDTO(
        @OptionalNotBlank(message = "{address.street.notBlank}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.GREEK_LATIN_TEXT,
                message = "{address.street.invalid}", groups = SecondOrder.class)
        String street,

        @OptionalNotBlank(message = "{address.number.notBlank}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.STREET_NUMBER,
                message = "{address.number.invalid}", groups = SecondOrder.class)
        String streetNumber,

        @OptionalNotBlank(message = "{address.city.notBlank}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.GREEK_LATIN_TEXT,
                message = "{address.city.invalid}", groups = SecondOrder.class)
        String city,

        @OptionalNotBlank(message = "{address.zip.notBlank}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.POSTAL_CODE,
                message = "{address.zip.invalid}", groups = SecondOrder.class)
        String zipCode
) {}