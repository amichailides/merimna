package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.validation.ValidationPatterns;
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
public record AddressDTO (
    @NotBlank(message = "{address.street.required}")
    @Pattern(regexp = ValidationPatterns.GREEK_LATIN_TEXT,
            message = "{address.street.invalid}")
    String street,

    @NotBlank(message = "{address.number.required}")
    @Pattern(regexp = ValidationPatterns.STREET_NUMBER,
            message = "{address.number.invalid}")
    String streetNumber,

    @NotBlank(message = "{address.city.required}")
    @Pattern(regexp = ValidationPatterns.GREEK_LATIN_TEXT,
            message = "{address.city.invalid}")
    String city,


    @NotBlank(message = "{address.zip.required}")
    @Pattern(regexp = ValidationPatterns.POSTAL_CODE,
            message = "{address.zip.invalid}")
    String zipCode
) {}

