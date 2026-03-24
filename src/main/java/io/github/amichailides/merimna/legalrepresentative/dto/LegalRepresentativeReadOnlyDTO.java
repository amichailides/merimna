package io.github.amichailides.merimna.legalrepresentative.dto;

import io.github.amichailides.merimna.domain.LegalRepresentativeType;
import lombok.Builder;

@Builder
public record LegalRepresentativeReadOnlyDTO(
        Long id,
        LegalRepresentativeType type,
        String firstName,
        String lastName,
        String mobileNumber,
        String landlinePhone,
        String email,
        String notes
) {}
