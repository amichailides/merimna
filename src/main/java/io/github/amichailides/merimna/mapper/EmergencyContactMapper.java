package io.github.amichailides.merimna.mapper;

import io.github.amichailides.merimna.dto.EmergencyContactDTO;
import io.github.amichailides.merimna.dto.EmergencyContactUpdateDTO;
import io.github.amichailides.merimna.model.EmergencyContact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmergencyContactMapper {
    private final AddressMapper addressMapper;

    public EmergencyContactDTO toDTO(EmergencyContact entity) {
        if (entity == null) return null;

        return EmergencyContactDTO.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .relationshipType(entity.getRelationshipType())
                .landlinePhone(entity.getLandlinePhone())
                .mobileNumber(entity.getMobileNumber())
                .email(entity.getEmail())
                .address(addressMapper.toDTO(entity.getAddress()))
                .build();
    }

    public EmergencyContact toEntity(EmergencyContactDTO dto) {
        if (dto == null) return null;

        return EmergencyContact.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .relationshipType(dto.relationshipType())
                .landlinePhone(dto.landlinePhone())
                .mobileNumber(dto.mobileNumber())
                .email(dto.email())
                .address(addressMapper.toEntity(dto.address()))
                .build();
    }

    public void updateEntity(EmergencyContact existing, EmergencyContactUpdateDTO dto) {
        if (dto == null || existing == null) return;

        if (dto.firstName() != null) existing.setFirstName(dto.firstName());
        if (dto.lastName() != null) existing.setLastName(dto.lastName());
        if (dto.relationshipType() != null) existing.setRelationshipType(dto.relationshipType());
        if (dto.landlinePhone() != null) existing.setLandlinePhone(dto.landlinePhone());
        if (dto.mobileNumber() != null) existing.setMobileNumber(dto.mobileNumber());
        if (dto.email() != null) existing.setEmail(dto.email());
        if (dto.address() != null) {
            addressMapper.updateEntity(existing.getAddress(), dto.address());
        }
    }

}

    // TODO: Re-evaluate mapper strategy for EmergencyContact updates.
    // If nested update flows become more complex, consider adding
    // updateEntityFromDto(...) instead of always rebuilding the embeddable.

