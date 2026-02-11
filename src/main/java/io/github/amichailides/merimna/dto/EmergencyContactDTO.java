package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.validation.ValidFirstName;
import io.github.amichailides.merimna.validation.ValidLastName;
import io.github.amichailides.merimna.validation.ValidPhone;
import io.github.amichailides.merimna.validation.ValidationPatterns;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record EmergencyContactDTO (
        @ValidFirstName(message = "{emergency.firstName.invalid}")
        String firstName,

        @ValidLastName(message = "{emergency.lastName.invalid}")
        String lastName,

        @NotBlank(message = "{emergency.relationship.required}")
        @Pattern(regexp = ValidationPatterns.GREEK_LATIN_TEXT,
                message = "{emergency.relationship.invalid}")
        String relationship,

        @ValidPhone(message = "{emergency.phone.invalid}")
        String phoneNumber,

        @ValidPhone(message = "{emergency.mobile.invalid}")
        String mobileNumber,

        @Email(message = "{emergency.email.invalid}")
        String email,

        @NotNull(message = "{address.required}")
        @Valid // ΑΠΑΡΑΙΤΗΤΟ: Για να ελεγχθούν τα @Pattern μέσα στο AddressDTO
        AddressDTO address
) {
        /*
         * TODO: ΥΠΟΧΡΕΩΤΙΚΗ ΥΛΟΠΟΙΗΣΗ (Business Requirement):
         * Πρέπει να προστεθεί Class-level Validator (π.χ. @AtLeastOneContactPresent)
         * που να διασφαλίζει ότι ΤΟΥΛΑΧΙΣΤΟΝ ΕΝΑ από τα πεδία:
         * 1. phoneNumber
         * 2. mobileNumber
         * είναι συμπληρωμένο. Δεν επιτρέπεται η αποθήκευση επαφής χωρίς κανένα μέσο επικοινωνίας.
         */
}