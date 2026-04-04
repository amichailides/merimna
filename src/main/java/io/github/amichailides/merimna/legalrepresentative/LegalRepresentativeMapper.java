package io.github.amichailides.merimna.legalrepresentative;

import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeUpdateDTO;
import io.github.amichailides.merimna.domain.LegalRepresentative;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LegalRepresentativeMapper {
    public LegalRepresentativeReadOnlyDTO toReadOnlyDTO(LegalRepresentative existing) {
        if (existing == null) return null;

        return LegalRepresentativeReadOnlyDTO.builder()
                .id(existing.getId())
                .type(existing.getType())
                .firstName(existing.getFirstName())
                .lastName(existing.getLastName())
                .mobileNumber(existing.getMobileNumber())
                .landlinePhone(existing.getLandlinePhone())
                .email(existing.getEmail())
                .notes(existing.getNotes())
                .build();
    }

    public LegalRepresentative toEntity(LegalRepresentativeDTO dto) {
        if (dto == null) return null;

        return LegalRepresentative.builder()
                .afm(dto.afm())
                .type(dto.type())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .mobileNumber(dto.mobileNumber())
                .landlinePhone(dto.landlinePhone())
                .email(dto.email())
                .notes(dto.notes())
                .build();
    }

    public void updateEntity(LegalRepresentative existing, LegalRepresentativeUpdateDTO dto) {
        Objects.requireNonNull(existing, "existing legal representative must not be null");
        Objects.requireNonNull(dto, "legal representative update dto must not be null");

        if (dto.type() != null) existing.setType(dto.type());
        if (dto.firstName() != null) existing.setFirstName(dto.firstName());
        if (dto.lastName() != null) existing.setLastName(dto.lastName());
        if (dto.mobileNumber() != null) existing.setMobileNumber(dto.mobileNumber());
        if (dto.landlinePhone() != null) existing.setLandlinePhone(dto.landlinePhone());
        if (dto.email() != null) existing.setEmail(dto.email());
        if (dto.notes() != null) existing.setNotes(dto.notes());

    }

}
