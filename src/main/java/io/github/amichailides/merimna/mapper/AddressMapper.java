package io.github.amichailides.merimna.mapper;

import io.github.amichailides.merimna.dto.AddressDTO;
import io.github.amichailides.merimna.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDTO fromEntity(Address address) {
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
}

