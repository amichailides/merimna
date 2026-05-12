package io.github.amichailides.merimna.address.dto;

import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * TODO: Replace free-text city/postal-code input with country-aware address validation
 * or lookup/autocomplete support when address handling becomes more advanced.
 */

@Schema(name = "AddressDTO", description = "Address payload")
@Builder
public record AddressDTO(
        @Schema(description = "Street name", example = "Ηρώων & Τσαλδάρη")
        @NotBlank(message = "{address.street.required}", groups = FirstOrder.class)
        @ValidGreekLatinText(
                extended = true,
                message = "{address.street.invalid}",
                groups = SecondOrder.class
        )
        String street,

        @Schema(description = "Street number", example = "12Α")
        @NotBlank(message = "{address.number.required}", groups = FirstOrder.class)
        @Pattern(
                regexp = ValidationPatterns.STREET_NUMBER,
                message = "{address.number.invalid}",
                groups = SecondOrder.class
        )
        String streetNumber,

        @Schema(description = "City name", example = "Αθήνα")
        @NotBlank(message = "{address.city.required}", groups = FirstOrder.class)
        @ValidGreekLatinText(
                message = "{address.city.invalid}",
                groups = SecondOrder.class
        )
        String city,

        @Schema(description = "Postal code", example = "10431")
        @NotBlank(message = "{address.zip.required}", groups = FirstOrder.class)
        @Pattern(
                regexp = ValidationPatterns.POSTAL_CODE,
                message = "{address.zip.invalid}",
                groups = SecondOrder.class
        )
        String zipCode
) {}

