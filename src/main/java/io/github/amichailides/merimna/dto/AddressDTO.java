package io.github.amichailides.merimna.dto;

import lombok.Builder;


@Builder
public record AddressDTO (
    String street,
    String streetNumber,
    String city,
    String zipCode
) {}

