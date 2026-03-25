package io.github.amichailides.merimna.address.dto;

import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * TODO: Για σοβαρό project, η ελεύθερη πληκτρολόγηση πόλης και Τ.Κ.
 * θα πρέπει να αντικατασταθεί από:
 * 1. Dropdown επιλογή από Look-up Tables (ISO Country Codes / Cities Database).
 * 2. Χρήση Address Autocomplete API (π.χ. Google Places API) για διασφάλιση εγκυρότητας διευθύνσεων.
 * 3. Δυναμικό Zip Code Validation ανάλογα με την επιλεγμένη χώρα.
 */

@Builder
public record AddressDTO(
        @Schema(description = "Street name", example = "Αθηνάς")
        @NotBlank(message = "{address.street.required}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.GREEK_LATIN_TEXT,
                message = "{address.street.invalid}", groups = SecondOrder.class)
        String street,

        @Schema(description = "Street number", example = "12Α")
        @NotBlank(message = "{address.number.required}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.STREET_NUMBER,
                message = "{address.number.invalid}", groups = SecondOrder.class)
        String streetNumber,

        @Schema(description = "City name", example = "Αθήνα")
        @NotBlank(message = "{address.city.required}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.GREEK_LATIN_TEXT,
                message = "{address.city.invalid}", groups = SecondOrder.class)
        String city,

        @Schema(description = "5-digit Greek postal code", example = "10431")
        @NotBlank(message = "{address.zip.required}", groups = FirstOrder.class)
        @Pattern(regexp = ValidationPatterns.POSTAL_CODE,
                message = "{address.zip.invalid}", groups = SecondOrder.class)
        String zipCode
) {}

