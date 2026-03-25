package io.github.amichailides.merimna.address;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.address.dto.AddressUpdateDTO;
import io.github.amichailides.merimna.domain.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDTO toDTO(Address address) {
        if (address == null) return null;

        return AddressDTO.builder()
                .street(address.getStreet())
                .streetNumber(address.getStreetNumber())
                .city(address.getCity())
                .zipCode(address.getZipCode())
                .build();
    }

    public Address toEntity(AddressDTO dto) {
        if (dto == null) return null;

        return Address.builder()
                .street(dto.street())
                .streetNumber(dto.streetNumber())
                .city(dto.city())
                .zipCode(dto.zipCode())
                .build();
    }

    public void updateEntity(Address existing, AddressUpdateDTO dto) {
        if (dto == null || existing == null) return;

        if (dto.street() != null) existing.setStreet(dto.street());
        if (dto.streetNumber() != null) existing.setStreetNumber(dto.streetNumber());
        if (dto.city() != null) existing.setCity(dto.city());
        if (dto.zipCode() != null) existing.setZipCode(dto.zipCode());
    }
}

