package io.github.amichailides.merimna.legalrepresentative;

import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeCreateDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeReadOnlyDTO;
import io.github.amichailides.merimna.legalrepresentative.dto.LegalRepresentativeUpdateDTO;
import io.github.amichailides.merimna.domain.LegalRepresentative;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;

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

    public LegalRepresentative toEntity(LegalRepresentativeCreateDTO dto) {
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

        updateIfNotNull(dto.type(), existing::setType);

        updateIfNotBlank(dto.firstName(), existing::setFirstName);
        updateIfNotBlank(dto.lastName(), existing::setLastName);
        updateIfNotBlank(dto.mobileNumber(), existing::setMobileNumber);
        updateIfNotBlank(dto.email(), existing::setEmail);

        // allow clearing landline phone
        if (dto.landlinePhone() != null) {
            existing.setLandlinePhone(dto.landlinePhone());
        }

        updateIfNotNull(dto.notes(), existing::setNotes);
    }

    private void updateIfNotBlank(String newValue, Consumer<String> setter) {
        if (newValue != null && !newValue.isBlank()) {
            setter.accept(newValue);
        }
    }

    private <T> void updateIfNotNull(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }

}
