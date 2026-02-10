package io.github.amichailides.merimna.dto;

import lombok.Builder;

// TODO: Implement custom class-level validator (e.g., @AtLeastOneContactPresent)
// to ensure that at least one of phoneNumber, mobileNumber, or email is provided.
@Builder
public record EmergencyContactDTO (
        String firstName,
        String lastName,
        String relationship,
        String phoneNumber,
        String mobileNumber,
        String email,
        AddressDTO address
) {}