package io.github.amichailides.merimna.address.dto;

import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.OptionalNotBlank;
import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Schema(name = "AddressUpdateDTO", description = "Partial address update payload")
@Builder
public record AddressUpdateDTO(

        @Schema(
                description = "Street name. Optional in PATCH requests.",
                example = "Ηρώων & Τσαλδάρη",
                maxLength = 100
        )
        @OptionalNotBlank(message = "{address.street.notBlank}", groups = FirstOrder.class)
        @ValidGreekLatinText(
                max = 100,
                extended = true,
                message = "{address.street.invalid}",
                groups = SecondOrder.class)
        String street,

        @Schema(
                description = "Street number. Optional in PATCH requests.",
                example = "12Α",
                maxLength = 20
        )
        @OptionalNotBlank(message = "{address.number.notBlank}", groups = FirstOrder.class)
        @Pattern(
                regexp = ValidationPatterns.STREET_NUMBER,
                message = "{address.number.invalid}",
                groups = SecondOrder.class)
        String streetNumber,

        @Schema(
                description = "City name. Optional in PATCH requests.",
                example = "Αθήνα",
                maxLength = 100
        )
        @OptionalNotBlank(message = "{address.city.notBlank}", groups = FirstOrder.class)
        @ValidGreekLatinText(
                max = 100,
                message = "{address.city.invalid}",
                groups = SecondOrder.class)
        String city,

        @Schema(
                description = "Postal code. Optional in PATCH requests.",
                example = "10431",
                maxLength = 10
        )
        @OptionalNotBlank(message = "{address.zip.notBlank}", groups = FirstOrder.class)
        @Pattern(
                regexp = ValidationPatterns.POSTAL_CODE,
                message = "{address.zip.invalid}",
                groups = SecondOrder.class)
        String zipCode
) {}