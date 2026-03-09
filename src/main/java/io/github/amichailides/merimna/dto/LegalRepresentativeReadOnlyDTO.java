package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.LegalRepresentativeType;
import lombok.Builder;

@Builder
public record LegalRepresentativeReadOnlyDTO(
        LegalRepresentativeType type,
        String firstName,
        String lastName,
        String mobileNumber,
        String landlinePhone,
        String email,
        String notes
) {}
