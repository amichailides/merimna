package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.groups.SecondOrder;

import jakarta.validation.constraints.Pattern;

public record AddressUpdateDTO(
        @Pattern(regexp = ValidationPatterns.GREEK_LATIN_TEXT,
                message = "{address.street.invalid}", groups = SecondOrder.class)
        String street,

        @Pattern(regexp = ValidationPatterns.STREET_NUMBER,
                message = "{address.number.invalid}", groups = SecondOrder.class)
        String streetNumber,

        @Pattern(regexp = ValidationPatterns.GREEK_LATIN_TEXT,
                message = "{address.city.invalid}", groups = SecondOrder.class)
        String city,

        @Pattern(regexp = ValidationPatterns.POSTAL_CODE,
                message = "{address.zip.invalid}", groups = SecondOrder.class)
        String zipCode
) {}
